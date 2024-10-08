package com.example.video_game_collections.Screens



import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.annotation.Size
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import com.example.video_game_collections.allViewModels.fireBaseAuthViewModel
import com.example.video_game_collections.allViewModels.loginStatus

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SuspiciousIndentation")
@Composable
fun signInScreen(navController: NavController, viewModel: fireBaseAuthViewModel) {

    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var username by remember {
        mutableStateOf("")
    }
    var shopname by remember {
        mutableStateOf("")
    }
    
    var state by remember {
        mutableStateOf(false)
    }
    var expanded by remember {
        mutableStateOf(false)
    }


    val role = if(!state){
        "customer"
    } else{
        "seller"
    }

    val shoptype= listOf("Xerox","Grocery","Stationary","Bakery")
    var selecteditem by remember {
        mutableStateOf(shoptype[0])
    }


    var context = LocalContext.current
    val observedLoginStatus  = viewModel.loginStatusState.observeAsState()

    LaunchedEffect(key1 = observedLoginStatus.value) {

        when(observedLoginStatus.value){
            //is loginStatus.Error -> TODO()
            is loginStatus.LoggedIn -> {


                viewModel.getUserRole {role ->
                    if(role == "customer"){
                        navController.navigate(NavigationPages.customerPage)
                    }else if(role == "seller"){
                        navController.navigate(NavigationPages.sellerPage)
                    }
                }

            }
            is loginStatus.LoggedOut -> {
                Toast.makeText(context,"error", Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }



    }






    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(

            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = "SignUp Page"
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                },
                label = { Text(text = "Enter UserName") },

                keyboardOptions = KeyboardOptions.Default
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                },
                label = { Text(text = "Enter Email") },


                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                )
            )

            Spacer(modifier = Modifier.height(24.dp))



            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                },
                label = { Text(text = "Enter Password") },

                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                )
            )
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.offset(x=(-8).dp)
            ){


                Text(text = "Customer",

                    color = if (!state){
                        MaterialTheme.colorScheme.tertiary
                    } else {
                        MaterialTheme.colorScheme.primary
                    }
                )
                Spacer(modifier = Modifier.width(20.dp))
                Switch(
                    checked = state,
                    onCheckedChange = {
                        state = it
                    },
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                        uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        uncheckedThumbColor = MaterialTheme.colorScheme.primary
                    )
                )


                Spacer(modifier = Modifier.width(20.dp))
                Text(text = "Seller",
                    color = if (state){
                        MaterialTheme.colorScheme.tertiary
                    } else {
                        MaterialTheme.colorScheme.primary
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if(state) {
                OutlinedTextField(
                    value = shopname,
                    onValueChange = {
                        shopname = it
                    },
                    label = { Text(text = "Enter ShopName") },

                    keyboardOptions = KeyboardOptions.Default
                )

                Spacer(modifier = Modifier.height(24.dp))

                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = {expanded = !expanded})
                {
                    OutlinedTextField(
                        modifier = Modifier.menuAnchor(),
                        value = selecteditem,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },

                        )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded=false }) {
                        shoptype.forEachIndexed { index, text ->
                            DropdownMenuItem(text = { Text(text = text)}, onClick = {
                                selecteditem= shoptype[index]
                                expanded=false
                            },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

            }
            Button(onClick = {

                Log.i("response","sign in clicked ${role}")

                if(role == "seller"){
                    viewModel.siginInUser(
                        userName = username,
                        email = email,
                        password = password,
                        role = role,
                        shopType = selecteditem,
                        shopName = shopname
                    )
                }else if(role == "customer"){
                    viewModel.siginInUser(
                        userName = username,
                        email = email,
                        password = password,
                        role = role,
                        shopType = null,
                        shopName = null
                    )
                }

            },
                modifier = Modifier.align(Alignment.CenterHorizontally),

            ) {

                Text(text = "Sigin In")

            }


            TextButton(
                onClick = {

                    navController.navigate(NavigationPages.loginPage)

                },
                modifier = Modifier.align(Alignment.CenterHorizontally),
            ) {

                Text(text = "Already have an account ? head back to Login")

            }


        }
    }

}