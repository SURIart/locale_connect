package com.example.video_game_collections.Screens.CustomerScreens


import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.video_game_collections.Screens.NavigationPages
import com.example.video_game_collections.allViewModels.fireBaseAuthViewModel
import com.example.video_game_collections.allViewModels.fireStoreViewModel
import com.example.video_game_collections.allViewModels.ordersCustomerSideViewModel
import com.example.video_game_collections.dataModels.OrderStatus
import com.example.video_game_collections.dataModels.productModel
import com.example.video_game_collections.dataModels.productOrderModel

@Composable
fun allProductsForCustomerByThisSeller(
    navController: NavController,
    modifier: Modifier = Modifier,
    fireStoreViewModel: fireStoreViewModel,
    authViewModel: fireBaseAuthViewModel,
    ordersCustomerSideViewModel: ordersCustomerSideViewModel
) {

    
    var observeAllProductsBySellerState = fireStoreViewModel.allProductsBySellerState.observeAsState(
        emptyList<productModel>()
    )

    var observedProductsCountMapState = ordersCustomerSideViewModel.productsCountMapState.observeAsState(
        emptyMap<String,Int>()
    )

    var shopName by remember {
        mutableStateOf("")
    }

    var context = LocalContext.current

    var productCounts by remember {
        mutableStateOf(mutableMapOf<String, Int>())
    }



    var buyerId = authViewModel.auth.currentUser?.uid

    var obserevedTotalCost = ordersCustomerSideViewModel.totalCost.observeAsState()




    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = "All products by  this seller")
        Log.i("changeOrder","allProductsForCustomerByThisSeller pagw  "+observeAllProductsBySellerState.value.toString())
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxHeight(0.6f),
            verticalArrangement = Arrangement.Center,
        ) {

            items(observeAllProductsBySellerState.value){

                Log.i("shopName"," inside your ")


                authViewModel.getShopName(it.sellerID){
                    shopName = it
                }


                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(16.dp),


                    ) {

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = it.imageURL,
                            contentDescription = null,
                            modifier = Modifier.height(120.dp),
                            contentScale = ContentScale.Crop
                        )
                        Text(text = it.pName)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = it.pCost.toString())
                        Log.i("shopName"," ${shopName}")
                        Text(text = shopName)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            IconButton(onClick = {

                                var count = ordersCustomerSideViewModel.getCurrCount(it.pName)

                                ordersCustomerSideViewModel.incrementOrderCount(it.pName,count) // changes the obsereved map
                                Log.i("orderIncDec","${ordersCustomerSideViewModel.getCurrCount(it.pName)} is ${it.pName}")

                                var tempProductOrderModel = productOrderModel(
                                     pName = it.pName,
                                 pCost = it.pCost,
                                 quantity  = ordersCustomerSideViewModel.getCurrCount(it.pName),
                                 totalProductCost =  it.pCost*ordersCustomerSideViewModel.getCurrCount(it.pName),
                                 sellerID = it.sellerID,
                                 imageURL  = it.imageURL,
                                    buyerID = buyerId,
                                    status = OrderStatus.PENDING,
                                    pID = it.pID


                                )

                                ordersCustomerSideViewModel.addOrUpdateToProductsInCartList(tempProductOrderModel)

                                ordersCustomerSideViewModel.addToTotalCost(it.pCost)

                            }) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null
                                )

                            }


                            if(observedProductsCountMapState.value.get(it.pName) == null){
                                Text(text = "0" )

                            }else{
                                Text(text =  observedProductsCountMapState.value.get(it.pName).toString() )

                            }




                            IconButton(onClick = {

                                var count = ordersCustomerSideViewModel.getCurrCount(it.pName)

                                // changes the obsereved map
                                if(count > 0){
                                    ordersCustomerSideViewModel.decrementCount(it.pName,count)
                                }
                                Log.i("orderIncDec","${ordersCustomerSideViewModel.getCurrCount(it.pName)} is ${it.pName}")

                                var tempProductOrderModel = productOrderModel(
                                    pName = it.pName,
                                    pCost = it.pCost,
                                    quantity  = ordersCustomerSideViewModel.getCurrCount(it.pName),
                                    totalProductCost =  it.pCost*ordersCustomerSideViewModel.getCurrCount(it.pName),
                                    sellerID = it.sellerID,
                                    imageURL  = it.imageURL,
                                    buyerID = buyerId,
                                    status = OrderStatus.PENDING,
                                    pID = it.pID


                                )

                                if(ordersCustomerSideViewModel.getCurrCount(it.pName) == 0){
                                    ordersCustomerSideViewModel.removeProductsInCartList(tempProductOrderModel)
                                }else{
                                    ordersCustomerSideViewModel.addOrUpdateToProductsInCartList(tempProductOrderModel)
                                }

                                if(ordersCustomerSideViewModel.getCurrCount(it.pName) >= 0){
                                    ordersCustomerSideViewModel.reduceFromTotalCost(it.pCost)

                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = null
                                )

                            }
                        }






                    }

                }

            }


        }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = {

                    navController.navigate(NavigationPages.addToCartPage)

                }) {

                    Text(text = "Check Out Page/ Cart Page")

                }
                Text(text = "Total Cost : ${obserevedTotalCost.value}")
            }
        }
    }

}