import { Request, Response } from "express";
import { leagueService } from "../services/league.service";

// Utils

/**
 * Generate a random league code
 * @param length - Length of the generated code
 * @returns a random code from digits and capital and small letters
 */
const generateLeagueCode = (length: number): string => {
    let result = "";
    const characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    const charactersLength = characters.length;
    for (let counter = 0; counter < length; counter++) {
        result += characters.charAt(Math.floor(Math.random() * charactersLength));
    }
    return result;
};

/**
 * Checks if the league code is unique i.e. no league with the same code exists
 * @param code - The code to be checked
 * @returns true if the code is valid
 */
const checkValidCode = async (code: string): Promise<boolean> => {
    const league = await leagueService.getLeagueByCode(code);
    return league === null;
};

// Route handlers
export const createLeague = async (req: Request, res: Response) => {
    const userId = req.authUser.id;
    const { name } = req.body;

    // Generate random code
    let code = generateLeagueCode(6);
    let isValid = await checkValidCode(code);

    // Keep generating codes until we find a valid one
    while (!isValid) {
        code = generateLeagueCode(6);
        isValid = await checkValidCode(code);
    }

    try {
        const league = await leagueService.createLeague(name, userId, code);
        return res.status(201).json(league);
    } catch (e) {
        return res.status(400).json({error: "Error creating league"});
    }
};

export const joinLeague = async (req: Request, res: Response) => {
    const userId = req.authUser.id;
    const { code } : { code: string } = req.body;

    try {
        const league = await leagueService.getLeagueByCode(code);

        // Check if league was found
        if (!league) return res.status(400).json({error: "No league found"});

        // Check if user is already in league
        const existingUsers = await leagueService.getLeagueUsers(league.id);
        if (existingUsers.includes(userId)) return res.status(400).json({error: "User already in league"});

        const leagueUser = await leagueService.addLeagueUser(userId, league.id);

        return res.status(201).json(leagueUser);
    } catch(e) {
        return res.status(400).json({error: "Error joining league"});
    }
};

export const getUserLeagues = async (req: Request, res: Response) => {
    const userId = req.authUser.id;

    try {
        const leagues = await leagueService.getUserLeagues(userId);

        return res.status(200).json(leagues);
    } catch (error) {
        return res.status(400).json({error: "Error getting user leagues"});
    }
};

export * as leagueController from "./league.controller";