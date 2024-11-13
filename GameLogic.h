//
// Created by Adam Świerzyński on 23/10/2024.
//

#ifndef GAMELOGIC_H
#define GAMELOGIC_H
#include <vector>

class GameLogic {
public:
    GameLogic();

    void initializeGame();
    bool makeMove(int fromRow, int fromCol, int toRow, int toCol);
    const std::vector<std::vector<int>>& getBoardState() const;
    bool isGameOver() const;
    int getCurrentPlayer() const;
    void resetGame();
    void setBoardState(const std::vector<std::vector<int>>& boardState);

private:
    std::vector<std::vector<int>> board;
    int currentPlayer;
    bool gameOver;
    bool mustContinueCapturing = false;
    int capturingPieceRow = -1;
    int capturingPieceCol = -1;

    bool isValidMove(int fromRow, int fromCol, int toRow, int toCol, bool& isCapture, bool mandatoryCapture);
    bool hasValidMoves(int player);
    void promoteToKing(int row, int col);
    void removePiece(int row, int col);
    bool canCapture(int player);
    bool canCaptureAt(int player, int row, int col);


};
#endif //GAMELOGIC_H
