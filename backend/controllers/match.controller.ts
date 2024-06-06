import { Request, Response } from "express";
import { matchService } from "../services/match.service";
import { MatchStatus } from "../enums/MatchStatus";
import { Score } from "../types/Score";
import { predictionService } from "../services/prediction.service";
import { scoreService } from "../services/score.service";

/**
 * Calculates the score based on a prediction and the actual match result
 * @param trueHomeScore Actual home score
 * @param trueAwayScore Actual away score
 * @param predictedHomeScore Predicted home score
 * @param predictedAwayScore Predicted away score
 * @returns 3 for an exact prediction, 1 for a correct result and wrong goal amount, 0 for a wrong prediction
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

export const getCurrentSeason = async (req: Request, res: Response) => {
    try {
        const season = await matchService.getCurrentSeason();

        res.status(200).json({currentSeason: season});
    } catch (error) {
        res.status(400).json({error: "Error getting current season"});
    }
};

export const initializeMatches = async (req: Request, res: Response) => {
    const { matches } = req.body;

    try {
        const _matches = await matchService.insertMatches(matches);

        res.status(200).json(_matches);
    } catch (error) {
        res.status(400).json({error: "Error initializing matches"});
    }
};

export const getMatchesBySeason = async (req: Request, res: Response) => {
    const { season } = req.params;
    try {
        const matches = await matchService.getMatchesBySeason(season);
        res.status(200).json(matches);
    } catch (error) {
        res.status(400).json({error: "Error getting matches"});
    }
};

export const updateMatch = async (req: Request, res: Response) => {
    const { matchId, status, homeScore, awayScore, datetime } = req.body;

    try {
        const updatedMatch = await matchService.updateMatch(matchId, status, homeScore, awayScore, datetime);

        if (status !== MatchStatus.Played)
            return res.status(200).json(updatedMatch);

        // Calculate score when match is finished
        const matchPredictions = await predictionService.getMatchPredictions(matchId);

        const scores: Score[] = [];

        matchPredictions.forEach(prediction => {
            scores.push({
                user_id: prediction.user_id,
                match_id: prediction.match_id,
                score: calculateScore(updatedMatch.home_score, updatedMatch.away_score, prediction.home, prediction.away)
            });
        });

        await scoreService.addScores(scores);

        return res.status(200).json(updatedMatch);
    } catch (error) {
        return res.status(400).json({error: "Error getting matches"});
    }
};

export * as matchController from "./match.controller";