package fr.bowser.behaviortracker.home

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import fr.bowser.behaviortracker.R
import javax.inject.Inject

class HomeActivity : AppCompatActivity(), HomeContract.View {

    @Inject
    lateinit var presenter: HomePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setupGraph()
    }

    private fun setupGraph(){
        val build = DaggerHomeComponent.builder()
                .homeModule(HomeModule(this))
                .build()
        build.inject(this)
    }

}
