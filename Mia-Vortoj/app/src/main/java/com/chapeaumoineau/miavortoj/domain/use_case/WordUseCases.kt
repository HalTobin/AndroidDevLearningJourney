package com.chapeaumoineau.miavortoj.domain.use_case

import com.chapeaumoineau.miavortoj.domain.use_case.word.*

data class WordUseCases(val getWords: GetWordsUseCase,
                        val getWordsFromDictionary: GetWordsByDictionaryUseCase,
                        val getOldWordByDictionaryId: GetOldWordByDictionaryIdUseCase,
                        val deleteWordsFromDictionary: DeleteWordsFromDictionaryUseCase,
                        val deleteWord: DeleteWordUseCase,
                        val changeWordLastTimestamp: ChangeWordLastTimestampUseCase,
                        val changeWordNbPlayed: ChangeWordNbPlayedUseCase,
                        val changeWordNbSucceed: ChangeWordNbSucceedUseCase,
                        val addWord: AddWordUseCase,
                        val getWord: GetWordUseCase
)
