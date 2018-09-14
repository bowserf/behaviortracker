package fr.bowser.behaviortracker.rewardsrow

import fr.bowser.behaviortracker.event.EventManager
import fr.bowser.behaviortracker.inapp.InAppManager
import fr.bowser.behaviortracker.utils.MockitoUtils.any
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RewardsRowPresenterTest {

    @Mock
    private lateinit var screen: RewardsRowContract.Screen

    @Mock
    private lateinit var inAppManager: InAppManager

    @Mock
    private lateinit var eventManager: EventManager

    @Test
    fun onItemClickedCallPurchase() {
        // Given
        val presenter = RewardsRowPresenter(screen, inAppManager, eventManager)

        // When
        presenter.onItemClicked("sku")

        // Then
        Mockito.verify(inAppManager, Mockito.times(1)).purchase(Mockito.anyString(), any())
    }

    @Test
    fun onItemClickedSendClickBuyEvent() {
        // Given
        val presenter = RewardsRowPresenter(screen, inAppManager, eventManager)

        // When
        presenter.onItemClicked("sku")

        // Then
        Mockito.verify(eventManager, Mockito.times(1)).sendClickBuyInAppEvent("sku")
    }


}