package com.example.assignment2

class BoardState(val arimaaPieces: Array<Array<ArimaaPiece?>?>, val trap: List<ArimaaPiece?>) {
    override fun equals(state: Any?): Boolean {
        if (this === state) return true
        if (state !is BoardState) return false
        return arimaaPieces.contentDeepEquals(state.arimaaPieces)
    }

    override fun hashCode(): Int {
        var result = arimaaPieces.contentDeepHashCode()
        result = 31 * result + trap.hashCode()
        return result
    }
}
