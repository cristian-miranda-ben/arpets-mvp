package com.example.ar2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.ar2.ui.theme.AR2Theme
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.ar2.scenes.InicioScreen
import com.example.ar2.scenes.SplashScreen
import com.example.ar2.scenes.UiPetScreen
import com.example.ar2.ui.navigation.InicioScreen
import com.example.ar2.ui.navigation.SplashScreen
import com.example.ar2.ui.navigation.UiPetScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AR2Theme {
                val navController= rememberNavController()
                //rutas
                NavHost(
                    navController=navController,
                    startDestination = SplashScreen
                ){
                    composable <SplashScreen>{
                        SplashScreen(navController)
                    }
                    composable<InicioScreen> {
                        InicioScreen(navController)
                    }
                    composable<UiPetScreen> {
                        //pedirPermisoCamara()
                        //if (pedirPermisoCamara()){
                        UiPetScreen(navController)
                    }
                }
                }
            }
        }
    }
