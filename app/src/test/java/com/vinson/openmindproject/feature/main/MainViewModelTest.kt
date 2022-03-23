package com.vinson.openmindproject.feature.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vinson.datamodel.asset.Asset
import com.vinson.datamodel.asset.AssetDetail
import com.vinson.datamodel.asset.Collection
import com.vinson.datamodel.asset.Contract
import com.vinson.datamodel.base.Result
import com.vinson.openmindproject.BaseTest
import com.vinson.openmindproject.features.home.MainViewModel
import com.vinson.openmindproject.model.AssetRepository
import com.vinson.openmindproject.model.EthRepository
import com.vinson.openmindproject.util.GIVEN
import com.vinson.openmindproject.util.THEN
import com.vinson.openmindproject.util.Tag
import com.vinson.openmindproject.util.WHEN
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainViewModelTest : BaseTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    override fun setUp() {
        super.setUp()
        mockkObject(AssetRepository)
        every { AssetRepository.getInstance() } returns mockk()
    }

    @After
    override fun end() {
        super.end()
    }

    @Test
    fun testHomeState_giveExitDetail_thenPageStateMain() {
        @Tag(GIVEN)
        every { AssetRepository.getInstance().getAssets().flow } returns mockk()
        coEvery { AssetRepository.getInstance().getAssetDetail(any(), any()) } returns Result.Success(
            fakeDetail
        )
        val viewModel = MainViewModel(AssetRepository.getInstance(), EthRepository.getInstance())

        @Tag(WHEN)
        viewModel.handleEvent(MainViewModel.HomeEvent.OpenDetail(fakeAsset))
        viewModel.handleEvent(MainViewModel.HomeEvent.ExitDetail)

        @Tag(THEN)
        assertEquals(viewModel.state.pageState, MainViewModel.HomePageState.Main)
    }

    private val fakeAsset = Asset(
        0,
        "image",
        "name",
        Contract("address"),
        "token",
    )

    private val fakeDetail = AssetDetail(
        0,
        "image",
        "name",
        "description",
        Collection("name"),
        "link",
    )
}