package com.cin.testfeatures.basic_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cin.testfeatures.R
import com.cin.testfeatures.basic_compose.ui.theme.TestFeaturesTheme

class BasicComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppExampleLazyColumn()
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

//@Preview(showBackground = true)
@Composable
fun ExamplePreview() {
    Greeting(name = "EJEMPLO EN PREVIEW")
}

/**
 * SIN  COLUMN
 * Si se ponen componentes un tras otro estos se
 * enciman ya que todos son colocados en las
 * coordenadas 0,0
 * */
// @Preview(showBackground = true)
@Composable
fun AppExample() {
    Text(text = "Ejemplo 1")
    Text(text = "Ejemplo 2")
    Text(text = "Ejemplo 3")
}

/**
 * Si agregamos una columna como LinearLayout
 * se ordenan un componente despues del otro
 * (como parametros se agregan los
 * atributos del componente)
 * */
//@Preview(showBackground = true)
@Composable
fun AppExampleColumn() {
    Column(
        Modifier
            .fillMaxSize()  // Como un match_parent
            .background(Color.Red) // Con un background
    ) {
        /**
         * AGREGAR UNA IMAGEN
         * Image (atributos)
         * */
        Image(
            painter = painterResource(id = R.drawable.ic_airplane),
            contentDescription = "Test Image",
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Ejemplo 1",
            textAlign = TextAlign.Center,
            fontSize = 32.sp,
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Ejemplo 2",
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Ejemplo 3",
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AppExampleLazyColumn() {
    /**
     * Alternativa a un ScrollView,
     * optimiza memoria,
     * parecido funcionamiento a un RecyclerView
     * Solo puede contener Items
     * */
    LazyColumn(
        Modifier
            .fillMaxSize()  // Como un match_parent
            .background(Color.Red) // Con un background
    ) {
        /**
         * AGREGAR UNA IMAGEN
         * Image (atributos)
         * */
        item {
            Image(
                painter = painterResource(id = R.drawable.ic_airplane),
                contentDescription = "Test Image",
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Ejemplo 1",
                textAlign = TextAlign.Center,
                fontSize = 32.sp,
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Ejemplo 2",
                textAlign = TextAlign.Center,
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Ejemplo 3",
                textAlign = TextAlign.Center,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            )
        }
    }
}