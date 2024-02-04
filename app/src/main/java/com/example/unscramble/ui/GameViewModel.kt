package com.example.unscramble.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {

    private lateinit var currentWord: String
    private var _uiState = MutableStateFlow(GameUiState())
    private var prevGuessedWords: MutableSet<String> = mutableSetOf()

    // The asStateFlow() makes this mutable state flow a read-only state flow.
    val uiState = _uiState.asStateFlow()

    // This is called by lifecycle itself
    init {
        _uiState.value = GameUiState(
            guessScramble = generateRandomAndShuffleWord()
        )
    }

    private fun reset() {
        _uiState.value = GameUiState(
            guessScramble = generateRandomAndShuffleWord()
        )
    }

    private fun generateRandomAndShuffleWord(): String {
        currentWord = allWords.random()
        while (currentWord in prevGuessedWords) {
            currentWord = allWords.random()
        }
        prevGuessedWords.add(currentWord)
        val shuffleWord = currentWord.toCharArray()
        while (shuffleWord.contentEquals(currentWord.toCharArray())) {
            shuffleWord.shuffle()
        }
        Log.d("TMK", currentWord)
        Log.d("TMK", shuffleWord.concatToString())
        return shuffleWord.concatToString()
    }

    fun updateTypedScramble(newWord: String) {
        _uiState.update {
            it.copy(
                typedScramble = newWord
            )
        }
    }

    fun checkGuess() {
        if (uiState.value.typedScramble == currentWord) {
            _uiState.update {
                it.copy(
                    guessScramble = generateRandomAndShuffleWord(),
                    typedScramble = "",
                    unit = it.unit.inc(),
                    score = it.score.plus(SCORE_INCREASE),
                    guessWrong = false
                )
            }
        } else {

            _uiState.update {
                it.copy(
                    typedScramble = "",
                    guessWrong = true
                )
            }

        }
    }

    fun skipGuess() {
        _uiState.update {
            it.copy(
                typedScramble = "",
                unit = it.unit.inc(),
                guessWrong = false,
                guessScramble = generateRandomAndShuffleWord()
            )
        }
    }

    fun playAgain() {
        reset()
    }

}