#include "GameLogic.h"

GameLogic::GameLogic() : currentPlayer(1), gameOver(false), mustContinueCapturing(false), capturingPieceRow(-1), capturingPieceCol(-1) {
    initializeGame();
}

void GameLogic::initializeGame() {
    board = {
        {0, 1, 0, 1, 0, 1, 0, 1},
        {1, 0, 1, 0, 1, 0, 1, 0},
        {0, 1, 0, 1, 0, 1, 0, 1},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {2, 0, 2, 0, 2, 0, 2, 0},
        {0, 2, 0, 2, 0, 2, 0, 2},
        {2, 0, 2, 0, 2, 0, 2, 0}
    };

    currentPlayer = 1;
    gameOver = false;
    mustContinueCapturing = false;
    capturingPieceRow = -1;
    capturingPieceCol = -1;
}

bool GameLogic::makeMove(int fromRow, int fromCol, int toRow, int toCol) {
    if (gameOver) {
        return false;
    }

    bool isCapture = false;
    bool mandatoryCapture = mustContinueCapturing || canCapture(currentPlayer);

    if (mustContinueCapturing) {
        if (fromRow != capturingPieceRow || fromCol != capturingPieceCol) {
            return false;
        }
    }

    if (!isValidMove(fromRow, fromCol, toRow, toCol, isCapture, mandatoryCapture)) {
        return false;
    }

    int piece = board[fromRow][fromCol];
    board[toRow][toCol] = piece;
    board[fromRow][fromCol] = 0;

    if (isCapture) {
        int capturedRow = (fromRow + toRow) / 2;
        int capturedCol = (fromCol + toCol) / 2;
        removePiece(capturedRow, capturedCol);
    }

    promoteToKing(toRow, toCol);

    if (isCapture && canCaptureAt(currentPlayer, toRow, toCol)) {
        mustContinueCapturing = true;
        capturingPieceRow = toRow;
        capturingPieceCol = toCol;
        return true;
    } else {
        mustContinueCapturing = false;
        capturingPieceRow = -1;
        capturingPieceCol = -1;

        if (currentPlayer == 1) {
            currentPlayer = 2;
        } else {
            currentPlayer = 1;
        }
    }

    if (!hasValidMoves(currentPlayer)) {
        gameOver = true;
    }

    return true;
}

const std::vector<std::vector<int>>& GameLogic::getBoardState() const {
    return board;
}

bool GameLogic::isGameOver() const {
    return gameOver;
}

int GameLogic::getCurrentPlayer() const {
    return currentPlayer;
}

void GameLogic::resetGame() {
    initializeGame();
}

void GameLogic::setBoardState(const std::vector<std::vector<int>>& boardState) {
    if (boardState.size() == 8 && boardState[0].size() == 8) {
        board = boardState;
    }
}

bool GameLogic::isValidMove(int fromRow, int fromCol, int toRow, int toCol, bool& isCapture, bool mandatoryCapture) {
    isCapture = false;

    if (fromRow < 0 || fromRow >= 8 || fromCol < 0 || fromCol >= 8 ||
        toRow < 0 || toRow >= 8 || toCol < 0 || toCol >= 8) {
        return false;
    }

    int piece = board[fromRow][fromCol];
    int destination = board[toRow][toCol];

    if (piece == 0 || (piece != currentPlayer && piece != currentPlayer + 2)) {
        return false;
    }

    if (destination != 0) {
        return false;
    }

    int rowDiff = toRow - fromRow;
    int colDiff = toCol - fromCol;

    int direction;
    if (currentPlayer == 1) {
        direction = 1;
    } else {
        direction = -1;
    }

    bool isKing = (piece == 3 || piece == 4);

    if (abs(rowDiff) == 2 && abs(colDiff) == 2) {
        int middleRow = (fromRow + toRow) / 2;
        int middleCol = (fromCol + toCol) / 2;
        int middlePiece = board[middleRow][middleCol];

        if (middlePiece != 0 &&
            (middlePiece != currentPlayer && middlePiece != currentPlayer + 2)) {
            if (isKing) {
                isCapture = true;
                return true;
            }
            if (rowDiff == 2 * direction) {
                isCapture = true;
                return true;
            }
            return false;
        }
    }

    if (mandatoryCapture) {
        return false;
    }

    if (abs(rowDiff) == 1 && abs(colDiff) == 1) {
        if (isKing || rowDiff == direction) {
            isCapture = false;
            return true;
        }
    }

    return false;
}

void GameLogic::promoteToKing(int row, int col) {
    int piece = board[row][col];
    if (piece == 1 && row == 7) {
        board[row][col] = 3;
    } else if (piece == 2 && row == 0) {
        board[row][col] = 4;
    }
}

void GameLogic::removePiece(int row, int col) {
    board[row][col] = 0;
}

bool GameLogic::hasValidMoves(int player) {
    if (mustContinueCapturing && currentPlayer == player) {
        if (canCaptureAt(player, capturingPieceRow, capturingPieceCol)) {
            return true;
        } else {
            mustContinueCapturing = false;
            capturingPieceRow = -1;
            capturingPieceCol = -1;
            return false;
        }
    }

    if (canCapture(player)) {
        return true;
    }

    for (int row = 0; row < 8; ++row) {
        for (int col = 0; col < 8; ++col) {
            int piece = board[row][col];
            if (piece == player || piece == player + 2) {
                bool isKing = (piece == 3 || piece == 4);
                std::vector<std::pair<int, int>> directions;
                int direction;
                if (currentPlayer == 1) {
                    direction = 1;
                } else {
                    direction = -1;
                }
                if (isKing) {
                    directions = { {1, 1}, {1, -1}, {-1, 1}, {-1, -1} };
                } else {
                    directions = { {direction, 1}, {direction, -1} };
                }
                for (const auto& dir : directions) {
                    int newRow = row + dir.first;
                    int newCol = col + dir.second;
                    bool isCapture = false;
                    if (isValidMove(row, col, newRow, newCol, isCapture, false)) {
                        if (!isCapture) {
                            return true;
                        }
                    }
                }
            }
        }
    }

    return false;
}

bool GameLogic::canCapture(int player) {
    for (int row = 0; row < 8; ++row) {
        for (int col = 0; col < 8; ++col) {
            int piece = board[row][col];
            if (piece == player || piece == player + 2) {
                if (canCaptureAt(player, row, col)) {
                    return true;
                }
            }
        }
    }
    return false;
}

bool GameLogic::canCaptureAt(int player, int row, int col) {
    int piece = board[row][col];

    bool isKing = (piece == 3 || piece == 4);

    std::vector<std::pair<int, int>> directions;
    if (isKing) {
        directions = { {2, 2}, {2, -2}, {-2, 2}, {-2, -2} };
    } else {
        int direction;
        if (currentPlayer == 1) {
            direction = 1;
        } else {
            direction = -1;
        }
        directions = { {2 * direction, 2}, {2 * direction, -2} };
    }

    for (const auto& dir : directions) {
        int newRow = row + dir.first;
        int newCol = col + dir.second;
        bool isCapture = false;
        if (isValidMove(row, col, newRow, newCol, isCapture, false) && isCapture) {
            return true;
        }
    }

    return false;
}