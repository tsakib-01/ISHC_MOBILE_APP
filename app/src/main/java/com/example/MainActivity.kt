package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.presentation.viewmodel.AuthViewModel
import com.example.ui.IshcScreen
import com.example.ui.IshcViewModel
import com.example.ui.screens.*
import com.example.ui.theme.DarkNavy
import com.example.ui.theme.IshcTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: IshcViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IshcTheme {
                IshcMainApp(
                    viewModel = viewModel,
                    authViewModel = authViewModel
                )
            }
        }
    }
}

@Composable
fun IshcMainApp(
    viewModel: IshcViewModel,
    authViewModel: AuthViewModel
) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val matchFilter by viewModel.matchFilter.collectAsStateWithLifecycle()

    val authState by authViewModel.uiState.collectAsStateWithLifecycle()

    val user by viewModel.user.collectAsStateWithLifecycle()
    val studentProfile by viewModel.studentProfile.collectAsStateWithLifecycle()
    val allUniversities by viewModel.allUniversities.collectAsStateWithLifecycle()
    val favoriteUniversities by viewModel.favoriteUniversities.collectAsStateWithLifecycle()
    val documents by viewModel.documents.collectAsStateWithLifecycle()
    val applications by viewModel.applications.collectAsStateWithLifecycle()
    val bookings by viewModel.bookings.collectAsStateWithLifecycle()
    val seminars by viewModel.seminars.collectAsStateWithLifecycle()
    val experts by viewModel.experts.collectAsStateWithLifecycle()
    val adminStats by viewModel.adminStats.collectAsStateWithLifecycle()

    val activeVideoCallUrl by viewModel.activeVideoCallUrl.collectAsStateWithLifecycle()
    val activeVideoExpertName by viewModel.activeVideoExpertName.collectAsStateWithLifecycle()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    // Full screen WebRTC video call layout without top/bottom bar
    if (currentScreen == IshcScreen.VIDEO_CALL) {
        VideoCallScreen(
            roomUrl = activeVideoCallUrl,
            expertName = activeVideoExpertName,
            onEndCall = { viewModel.endVideoCall() }
        )
        return
    }

    // Full screen Auth Portal
    if (currentScreen == IshcScreen.AUTH) {
        AuthScreen(
            authState = authState,
            onLoginEmail = { email, pass ->
                authViewModel.loginWithEmail(email, pass)
                viewModel.navigateTo(IshcScreen.PROGRAMMES)
            },
            onRegisterEmail = { email, pass, name, role ->
                authViewModel.registerWithEmail(email, pass, name, role)
                viewModel.navigateTo(IshcScreen.PROGRAMMES)
            },
            onGoogleSignIn = { token, email, name ->
                authViewModel.loginWithGoogle(token, email, name)
                viewModel.navigateTo(IshcScreen.PROGRAMMES)
            },
            onResetPassword = { email -> authViewModel.resetPassword(email) },
            onClearMessages = { authViewModel.clearMessages() },
            onContinueAsGuest = { viewModel.navigateTo(IshcScreen.PROGRAMMES) }
        )
        return
    }

    val displayName = user?.name ?: "Alex Rivera"
    val userInitials = remember(displayName) {
        displayName.split(" ")
            .mapNotNull { it.firstOrNull() }
            .joinToString("")
            .take(2)
            .ifEmpty { "AR" }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = DarkNavy
            ) {
                DrawerNavigationContent(
                    currentScreen = currentScreen,
                    onSelectScreen = { screen -> viewModel.navigateTo(screen) },
                    onCloseDrawer = {
                        coroutineScope.launch { drawerState.close() }
                    },
                    userName = displayName,
                    userInitials = userInitials
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopBarHeader(
                    currentScreen = currentScreen,
                    searchQuery = searchQuery,
                    onSearchChange = { viewModel.setSearchQuery(it) },
                    onOpenDrawer = {
                        coroutineScope.launch { drawerState.open() }
                    },
                    onNavigateToProfile = { viewModel.navigateTo(IshcScreen.PROFILE) },
                    onNavigateToExperts = { viewModel.navigateTo(IshcScreen.EXPERTS) },
                    onLogout = {
                        authViewModel.logout()
                        viewModel.navigateTo(IshcScreen.AUTH)
                    },
                    userInitials = userInitials
                )
            },
            bottomBar = {
                AppBottomNavigationBar(
                    currentScreen = currentScreen,
                    onSelectScreen = { screen -> viewModel.navigateTo(screen) }
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (currentScreen) {
                    IshcScreen.PROGRAMMES -> {
                        ProgrammesScreen(
                            matchFilter = matchFilter,
                            onFilterChange = { ssc, hsc, ielts, country, maxFee ->
                                viewModel.updateMatchFilter(ssc, hsc, ielts, country, maxFee)
                            },
                            studentProfile = studentProfile,
                            universities = allUniversities,
                            searchQuery = searchQuery,
                            onToggleFavorite = { id, isFav -> viewModel.toggleFavorite(id, isFav) }
                        )
                    }

                    IshcScreen.FAVORITES -> {
                        FavoritesScreen(
                            favorites = favoriteUniversities,
                            studentSsc = matchFilter.sscGpa,
                            studentHsc = matchFilter.hscGpa,
                            studentIelts = matchFilter.ieltsScore,
                            onToggleFavorite = { id, isFav -> viewModel.toggleFavorite(id, isFav) }
                        )
                    }

                    IshcScreen.JOURNEY -> {
                        JourneyScreen(
                            studentProfile = studentProfile,
                            onUpdateStage = { stage ->
                                studentProfile?.let { prof ->
                                    viewModel.updateStudentProfile(
                                        prof.sscGpa, prof.hscGpa, prof.ieltsOverall,
                                        prof.ieltsReading, prof.ieltsWriting, prof.ieltsListening, prof.ieltsSpeaking,
                                        prof.preferredCountries, stage
                                    )
                                }
                            }
                        )
                    }

                    IshcScreen.DOCUMENTS -> {
                        DocumentsScreen(
                            documents = documents,
                            onAddDocument = { name, cat, fileName, size ->
                                viewModel.addDocument(name, cat, fileName, size)
                            },
                            onDeleteDocument = { id -> viewModel.deleteDocument(id) }
                        )
                    }

                    IshcScreen.APPLICATIONS -> {
                        ApplicationsScreen(
                            applications = applications,
                            onAddApplication = { uni, prog, country, intake, deadline, notes ->
                                viewModel.addApplication(uni, prog, country, intake, deadline, notes)
                            },
                            onUpdateStatus = { app, status ->
                                viewModel.updateApplicationStatus(app, status)
                            },
                            onDeleteApplication = { id -> viewModel.deleteApplication(id) }
                        )
                    }

                    IshcScreen.MEETINGS -> {
                        MeetingsScreen(
                            bookings = bookings,
                            experts = experts,
                            onStartVideoCall = { url, expert ->
                                viewModel.startVideoCall(url, expert)
                            },
                            onBookConsultation = { expert, title, time, topic ->
                                viewModel.bookConsultation(expert, title, time, topic)
                            }
                        )
                    }

                    IshcScreen.SEMINARS -> {
                        SeminarsScreen(
                            seminars = seminars,
                            onToggleRegistration = { id, isReg ->
                                viewModel.toggleSeminarRegistration(id, isReg)
                            }
                        )
                    }

                    IshcScreen.EXPERTS -> {
                        ExpertsScreen(
                            experts = experts,
                            onBookConsultation = { _, _ ->
                                viewModel.navigateTo(IshcScreen.MEETINGS)
                            }
                        )
                    }

                    IshcScreen.PROFILE -> {
                        ProfileScreen(
                            user = user,
                            studentProfile = studentProfile,
                            onSaveProfile = { ssc, hsc, overall, r, w, l, s, countries, stage ->
                                viewModel.updateStudentProfile(ssc, hsc, overall, r, w, l, s, countries, stage)
                            }
                        )
                    }

                    IshcScreen.ADMIN -> {
                        AdminDashboardScreen(
                            stats = adminStats,
                            universities = allUniversities,
                            applications = applications,
                            documents = documents,
                            onAddUniversity = { name, country, city, ssc, hsc, ielts, tuition ->
                                viewModel.addUniversity(name, country, city, ssc, hsc, ielts, tuition)
                            },
                            onUpdateApplicationStatus = { app, status ->
                                viewModel.updateApplicationStatus(app, status)
                            },
                            onVerifyDocument = { id, status ->
                                viewModel.verifyDocumentStatus(id, status)
                            }
                        )
                    }

                    IshcScreen.AUTH -> {
                        // Handled above
                    }

                    IshcScreen.VIDEO_CALL -> {
                        // Handled above
                    }
                }
            }
        }
    }
}
