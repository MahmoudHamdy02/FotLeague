import { Request, Response } from "express";
import { scoreService } from "../services/score.service";
import { matchService } from "../services/match.service";

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

export const getUserScores = async (req: Request, res: Response) => {
    const userId = req.authUser.id;

    try {
        const season = await matchService.getCurrentSeason();

        const scores = await scoreService.getUserScoresBySeason(userId, season);

        return res.status(200).json(scores);
    } catch (error) {
        return res.status(400).json({error: "Error getting user scores"});
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

export const getTopGlobalUsers = async (req: Request, res: Response) => {
    const num = parseInt((req.query.num ?? "10").toString());

    try {
        const season = await matchService.getCurrentSeason();

        const results = await scoreService.getTopGlobalUsersBySeason(num, season);

        return res.status(200).json(results);
    } catch (error) {
        console.log(error);
        return res.status(400).json({error: "Error getting top global users"});
    }
};

export * as scoreController from "./score.controller";