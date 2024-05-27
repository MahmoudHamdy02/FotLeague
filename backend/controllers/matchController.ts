import { Request, Response } from "express";
import * as matchService from "../services/matchService";

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