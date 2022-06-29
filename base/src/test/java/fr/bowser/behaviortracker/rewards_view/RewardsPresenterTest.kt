package fr.bowser.behaviortracker.rewards_view

import fr.bowser.behaviortracker.inapp.InAppConfiguration
import fr.bowser.behaviortracker.utils.MockitoUtils.any
import fr.bowser.feature.billing.InAppManager
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RewardsPresenterTest {

    @Mock
    private lateinit var screen: RewardsContract.Screen

    @Mock
    private lateinit var inAppConfiguration: InAppConfiguration

    @Mock
    private lateinit var inAppManager: InAppManager

    @Test
    fun startRegisterToInAppManager() {
        // Given
        val presenter = RewardsPresenter(screen, inAppConfiguration, inAppManager)

        // When
        presenter.onStart()

        // Then
        Mockito.verify(inAppManager).addListener(any())
    }

    @Test
    fun startCallDisplayListInApps() {
        // Given
        val presenter = RewardsPresenter(screen, inAppConfiguration, inAppManager)

        // When
        presenter.onStart()

        // Then
        Mockito.verify(screen).displayListInApps(any())
    }

    @Test
    fun stopUnregisterToInAppManager() {
        // Given
        val presenter = RewardsPresenter(screen, inAppConfiguration, inAppManager)

        // When
        presenter.onStop()

        // Then
        Mockito.verify(inAppManager).removeListener(any())
    }
}
