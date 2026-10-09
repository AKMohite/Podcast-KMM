package com.mak.pocketnotes.core.database.dao

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.mak.pocketnotes.core.common.coroutines.DispatcherProvider
import com.mak.pocketnotes.core.database.PocketNotesDatabase
import com.mak.pocketnotes.core.database.queries.Episodes
import com.mak.pocketnotes.core.database.queries.PocketDatabase
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.time.Instant
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class SQLDelightEpisodeDAOTest {
  private val testDispatcher = StandardTestDispatcher()
  private lateinit var driver: JdbcSqliteDriver
  private lateinit var dao: SQLDelightEpisodeDAO

  private val dispatcherProvider = object : DispatcherProvider {
    override val io: CoroutineDispatcher = testDispatcher
    override val computation: CoroutineDispatcher = testDispatcher
    override val main: CoroutineDispatcher = testDispatcher
  }

  @BeforeTest
  fun setUp() {
    Dispatchers.setMain(testDispatcher)
    driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
    PocketDatabase.Schema.create(driver)
    val notesDb = PocketNotesDatabase(driver).build()
    dao = SQLDelightEpisodeDAO(notesDb, dispatcherProvider)
  }

  @AfterTest
  fun tearDown() {
    driver.close()
    Dispatchers.resetMain()
  }

  @Test
  fun insertAndRetrieveEpisode() = runTest {
    val episode = Episodes(
      id = "ep-1",
      title = "Test Episode",
      description = "Test Desc",
      listen_notes_url = "https://url",
      thumbnail = "https://thumb",
      image = "https://img",
      audio = "https://audio",
      audio_length = 1200L,
      podcast_id = "pod-1",
      published_on = Instant.fromEpochSeconds(1000L),
      next_episode_published_on = null
    )

    dao.insert(episode)

    val retrieved = dao.getEpisodeById("ep-1")
    assertNotNull(retrieved)
    assertEquals("Test Episode", retrieved.title)
    assertEquals("pod-1", retrieved.podcast_id)
  }
}
