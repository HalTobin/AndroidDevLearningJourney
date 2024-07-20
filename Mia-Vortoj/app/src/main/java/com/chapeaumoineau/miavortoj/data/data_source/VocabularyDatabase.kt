package com.chapeaumoineau.miavortoj.data.data_source

import android.content.ContentValues
import androidx.room.Database
import androidx.room.OnConflictStrategy
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.chapeaumoineau.miavortoj.domain.model.Dictionary
import com.chapeaumoineau.miavortoj.domain.model.FavoriteLanguage
import com.chapeaumoineau.miavortoj.domain.model.Word


@Database(
    entities = [Dictionary::class, Word::class, FavoriteLanguage::class],
    version = 7
)
abstract class VocabularyDatabase: RoomDatabase() {

    abstract val dictionaryDao: DictionaryDao
    abstract val wordDao: WordDao
    abstract val favoriteLanguageDao: FavoriteLanguageDao

    companion object {
        const val DATABASE_NAME = "vocabulary_db"

        val prepopulate = object: RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                val contentValues = ContentValues()
                contentValues.put("id", 1)
                contentValues.put("iso", "DEU")
                contentValues.put("timestamp", 1)
                db.insert("favoriteLanguage", OnConflictStrategy.IGNORE, contentValues)

                contentValues.put("id", 2)
                contentValues.put("iso", "FRA")
                contentValues.put("timestamp", 2)
                db.insert("favoriteLanguage", OnConflictStrategy.IGNORE, contentValues)

                contentValues.put("id", 3)
                contentValues.put("iso", "ESP")
                contentValues.put("timestamp", 3)
                db.insert("favoriteLanguage", OnConflictStrategy.IGNORE, contentValues)

                contentValues.put("id", 4)
                contentValues.put("iso", "ENG")
                contentValues.put("timestamp", 4)
                db.insert("favoriteLanguage", OnConflictStrategy.IGNORE, contentValues)
            }
        }

        val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE 'Word' ADD COLUMN 'nbPlayed' INTEGER DEFAULT 0");
                database.execSQL("ALTER TABLE 'Word' ADD COLUMN 'nbSucceed' INTEGER DEFAULT 0");
            }
        }

        val MIGRATION_3_4: Migration = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("BEGIN TRANSACTION;");
                database.execSQL("CREATE TABLE WordNew('id' INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "'sourceWord' TEXT NOT NULL," +
                        "'targetWord' TEXT NOT NULL," +
                        "'emote' TEXT NOT NULL," +
                        "'notes' TEXT NOT NULL," +
                        "'difficulty' INTEGER NOT NULL," +
                        "'mastery' INTEGER NOT NULL DEFAULT 0," +
                        "'timestamp' INTEGER NOT NULL," +
                        "'lastTestTimestamp' INTEGER NOT NULL," +
                        "'nbPlayed' INTEGER NOT NULL DEFAULT 0," +
                        "'nbSucceed' INTEGER NOT NULL DEFAULT 0," +
                        "'dictionaryId' INTEGER NOT NULL," +
                        "'themeId' INTEGER NOT NULL)")
                database.execSQL("INSERT INTO WordNew(id,sourceWord,targetWord,emote,notes,difficulty,mastery,timestamp,lastTestTimestamp,nbPlayed,nbSucceed,dictionaryId,themeId) SELECT id,sourceWord,targetWord,emote,notes,difficulty,mastery,timestamp,lastTestTimestamp,nbPlayed,nbSucceed,dictionaryId,themeId FROM Word;");
                database.execSQL("DROP TABLE Word;");
                database.execSQL("ALTER TABLE 'WordNew' RENAME TO 'Word';");
                database.execSQL("COMMIT;");
            }
        }

        val MIGRATION_4_5: Migration = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("BEGIN TRANSACTION;");
                database.execSQL("CREATE TABLE WordNew('id' INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "'sourceWord' TEXT NOT NULL," +
                        "'targetWord' TEXT NOT NULL," +
                        "'emote' TEXT NOT NULL," +
                        "'notes' TEXT NOT NULL," +
                        "'timestamp' INTEGER NOT NULL," +
                        "'lastTestTimestamp' INTEGER NOT NULL," +
                        "'nbPlayed' INTEGER NOT NULL DEFAULT 0," +
                        "'nbSucceed' INTEGER NOT NULL DEFAULT 0," +
                        "'dictionaryId' INTEGER NOT NULL," +
                        "'themeId' INTEGER NOT NULL)")
                database.execSQL("INSERT INTO WordNew(id,sourceWord,targetWord,emote,notes,timestamp,lastTestTimestamp,nbPlayed,nbSucceed,dictionaryId,themeId) SELECT id,sourceWord,targetWord,emote,notes,timestamp,lastTestTimestamp,nbPlayed,nbSucceed,dictionaryId,themeId FROM Word;");
                database.execSQL("DROP TABLE Word;");
                database.execSQL("ALTER TABLE 'WordNew' RENAME TO 'Word';");
                database.execSQL("COMMIT;");
            }
        }

        val MIGRATION_5_6: Migration = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE 'Dictionary' ADD COLUMN 'number' INTEGER NOT NULL DEFAULT 0");
            }
        }

        val MIGRATION_6_7: Migration = object : Migration(6, 7) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("BEGIN TRANSACTION;");
                database.execSQL("CREATE TABLE DictionaryNew('id' INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "'title' TEXT NOT NULL," +
                        "'description' TEXT NOT NULL," +
                        "'languageIso' TEXT NOT NULL)")
                database.execSQL("INSERT INTO DictionaryNew(id,title,description,languageIso) SELECT id,title,description,languageIso FROM Dictionary;");
                database.execSQL("DROP TABLE Dictionary;");
                database.execSQL("ALTER TABLE 'DictionaryNew' RENAME TO 'Dictionary';");
                database.execSQL("COMMIT;");
            }
        }
    }

}