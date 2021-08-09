package com.tatam.thewheelycoolapp.data.room

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tatam.thewheelycoolapp.data.model.WheelItem
import com.tatam.thewheelycoolapp.data.model.WheelItemRepository
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class WheelItemsDatabaseTest : TestCase() {
    private lateinit var wheelItemDao: WheelItemDao
    private lateinit var db: WheelItemsDatabase
    private lateinit var repository: WheelItemRepository
    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Application>()
        db = Room.inMemoryDatabaseBuilder(
            context, WheelItemsDatabase::class.java
        ).build()
        wheelItemDao = db.wheelItemDao()
        repository = WheelItemRepository(wheelItemDao)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun writeAndReadSpend() = runBlocking {
        val wheelItem = WheelItem(0, "dog")
        wheelItemDao.addWheelItem(wheelItem)
        val items = wheelItemDao.getItems().blockingObserve()
        Assert.assertTrue(items?.contains(wheelItem)!!)
    }

    private fun <T> LiveData<T>.blockingObserve(): T? {
        var value: T? = null
        val latch = CountDownLatch(1)

        val observer = Observer<T> { t ->
            value = t
            latch.countDown()
        }

        observeForever(observer)

        latch.await(2, TimeUnit.SECONDS)
        return value
    }

    @Test
    fun checkIfWheelNameExists_Success() {
        runBlocking {
            repository.addItem(WheelItem(0, "Dog"))
            repository.addItem(WheelItem(1, "Cat"))
            repository.addItem(WheelItem(2, "Bird"))

            val expectedName = "Dog"
            assertEquals(repository.checkItemExists(expectedName), true)

            val expectedName2 = "Cat"
            assertEquals(repository.checkItemExists(expectedName), true)

            val expectedName3 = "Bird"
            assertEquals(repository.checkItemExists(expectedName), true)
        }
    }

    @Test
    fun checkIfWheelNameExists_Failure() {
        runBlocking {
            repository.addItem(WheelItem(0, "Dog"))
            repository.addItem(WheelItem(1, "Cat"))
            repository.addItem(WheelItem(2, "Bird"))

            val expectedName = "Fish"
            assertEquals(repository.checkItemExists(expectedName), false)

            val expectedName2 = "Lamb"
            assertEquals(repository.checkItemExists(expectedName), false)

            val expectedName3 = "Cow"
            assertEquals(repository.checkItemExists(expectedName), false)
        }
    }
}