import { Request, Response } from "express";
import * as matchService from "../services/matchService";

export const initializeMatches = async (req: Request, res: Response) => {
    const { matches } = req.body;

    try {
        const _matches = await matchService.insertMatches(matches);

        res.status(200).json(_matches);
    } catch (error) {
        console.log(error);
        res.status(400).json({error: "Error initializing matches"});
    }
};