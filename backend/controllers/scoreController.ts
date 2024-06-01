import { Request, Response } from "express";
import { Score } from "../types/Score";
import { scoreService } from "../services/scoreService";
import { MatchStatus } from "../enums/MatchStatus";
import { predictionService } from "../services/predictionService";
import { matchService } from "../services/matchService";

/**
 * Calculates the score based on a prediction and the actual match result
 * @param trueHomeScore Actual home score
 * @param trueAwayScore Actual away score
 * @param predictedHomeScore Predicted home score
 * @param predictedAwayScore Predicted away score
 * @returns 3 for an exact prediction
 * @returns 1 for a correct result and wrong goal amount
 * @returns 0 for a wrong prediction
 */
const calculateScore = (trueHomeScore: number, trueAwayScore: number, predictedHomeScore: number, predictedAwayScore: number): 0 | 1 | 3 => {
    // Exact prediction
    if (trueHomeScore === predictedHomeScore && trueAwayScore === predictedAwayScore)
        return 3;

    // Correct result but wrong goal count
    if (
        // Draw
        (trueHomeScore === trueAwayScore && predictedHomeScore === predictedAwayScore)
        ||
        // Home win
        (trueHomeScore > trueAwayScore && predictedHomeScore > predictedAwayScore)
        ||
        // Away win
        (trueHomeScore < trueAwayScore && predictedHomeScore < predictedAwayScore)
    )
        return 1;

    // Wrong result
    return 0;
};

export const getTotalUserScoreBySeason = async (req: Request, res: Response) => {
    const userId = req.authUser.id;
    const {season} = req.params;

    try {
        const score = await scoreService.getTotalUserScoreBySeason(userId, parseInt(season));

        return res.status(200).json({score});
    } catch (error) {
        return res.status(400).json({error: "Error getting user season score"});
    }
};

export const getUserScoresBySeason = async (req: Request, res: Response) => {
    const userId = req.authUser.id;
    const {season} = req.params;

    try {
        const scores = await scoreService.getUserScoresBySeason(userId, parseInt(season));

        return res.status(200).json(scores);
    } catch (error) {
        return res.status(400).json({error: "Error getting user scores"});
    }
};

// TODO: Validate request parameters and body
export const calculateScoresForMatch = async (req: Request, res: Response) => {
    const { matchId } = req.body;

    try {
        const match = await matchService.getMatchDetails(matchId);
        if (match.match_status !== MatchStatus.Played)
            return res.status(400).json({error: "Match must be played"});

        await matchService.updateMatchStatus(matchId, MatchStatus.Calculated);

        const matchPredictions = await predictionService.getMatchPredictions(matchId);

        const scores: Omit<Score, "id">[] = [];

        matchPredictions.forEach(prediction => {
            scores.push({
                user_id: prediction.user_id,
                match_id: prediction.match_id,
                score: calculateScore(match.home_score, match.away_score, prediction.home, prediction.away)
            });
        });

        await scoreService.addScores(scores);

        return res.status(200).json({message: "OK"});
    } catch (error) {
        return res.status(400).json({error: "Error calculating scores"});
    }
};

export const getTopGlobalUsersBySeason = async (req: Request, res: Response) => {
    const {season} = req.params;
    const num = parseInt((req.query.num ?? "10").toString());

    try {
        const results = await scoreService.getTopGlobalUsersBySeason(num, parseInt(season));

        return res.status(200).json(results);
    } catch (error) {
        return res.status(400).json({error: "Error getting top global users"});
    }
};

export * as scoreController from "./scoreController";