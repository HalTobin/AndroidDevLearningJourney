package com.chapeaumoineau.miavortoj.domain.use_case

import com.chapeaumoineau.miavortoj.domain.use_case.dictionary.AddDictionaryUseCase
import com.chapeaumoineau.miavortoj.domain.use_case.dictionary.DeleteDictionaryUseCase
import com.chapeaumoineau.miavortoj.domain.use_case.dictionary.GetDictionariesUseCase
import com.chapeaumoineau.miavortoj.domain.use_case.dictionary.GetDictionaryUseCase

data class DictionaryUseCases(val getDictionaries: GetDictionariesUseCase,
                              val deleteDictionary: DeleteDictionaryUseCase,
                              val addDictionary: AddDictionaryUseCase,
                              val getDictionary: GetDictionaryUseCase
)
