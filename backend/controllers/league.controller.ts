import { Request, Response } from "express";
import { leagueService } from "../services/league.service";
import { validate } from "./utils";
import { LeagueDto } from "../types/LeagueDto";

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
    const league = await leagueService.getLeagueCodeByCode(code);
    return league === null;
};

/**
 * Returns a new league code that is guaranteed to be unused
 * @returns new unique code
 */
const getNewLeagueCode = async (): Promise<string> => {
    // Generate random code
    let code = generateLeagueCode(6);
    let isValid = await checkValidCode(code);

    // Keep generating codes until we find a valid one
    while (!isValid) {
        code = generateLeagueCode(6);
        isValid = await checkValidCode(code);
    }

    return code;
};

// Route handlers
export const createLeague = async (req: Request, res: Response) => {
    const userId = req.authUser.id;
    const { name } = req.body;
    if (!validate([name], ["string"], res)) return;

    const code = await getNewLeagueCode();

    try {
        const leagueCode = await leagueService.createLeagueCode(code);
        const league = await leagueService.createLeague(name, userId, leagueCode.id);

        const leagueDto: LeagueDto = {
            id: league.id,
            name: league.name,
            owner_id: league.owner_id,
            code: leagueCode.code
        };
        return res.status(201).json(leagueDto);
    } catch (e) {
        console.log(e);
        return res.status(400).json({error: "Error creating league"});
    }
};

export const joinLeague = async (req: Request, res: Response) => {
    const userId = req.authUser.id;
    const { code } = req.body;
    if (!validate([code], ["string"], res)) return;

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
        const leagues: LeagueDto[] = await leagueService.getUserLeagues(userId);

        return res.status(200).json(leagues);
    } catch (error) {
        return res.status(400).json({error: "Error getting user leagues"});
    }
};

export const getLeagueDetailsById = async (req: Request, res: Response) => {
    const { id } = req.params;

    try {
        const leagueId = parseInt(id);

        const league = await leagueService.getLeagueById(leagueId);

        if (!league) return res.status(400).json({error: "No league found"});

        const userScores = await leagueService.getLeagueUsersWithScores(leagueId);

        return res.status(200).json({league, userScores});
    } catch (error) {
        return res.status(400).json({error: "Error getting user leagues"});
    }
};

export const renameLeague = async (req: Request, res: Response) => {
    const userId = req.authUser.id;
    const { leagueId, name } = req.body;
    if (!validate([leagueId, name], ["number", "string"], res)) return;

    try {
        const league = await leagueService.getLeagueById(leagueId);

        if (!league) return res.status(400).json({error: "No league found"});

        if (league.owner_id !== userId) return res.status(400).json({error: "Can't rename league: unauthorized"});

        const renamedLeague = await leagueService.renameLeague(leagueId, name);

        res.status(200).json(renamedLeague);
    } catch (error) {
        return res.status(400).json({error: "Error renaming league"});
    }
};

export const generateNewLeagueCode = async (req: Request, res: Response) => {
    const userId = req.authUser.id;
    const { leagueId } = req.body;
    if (!validate([leagueId], ["number"], res)) return;

    try {
        const league = await leagueService.getLeagueById(leagueId);

        if (!league) return res.status(400).json({error: "No league found"});

        if (league.owner_id !== userId) return res.status(400).json({error: "Can't generate new code: Unauthorized"});

        const code = await getNewLeagueCode();

        const leagueCode = await leagueService.createLeagueCode(code);

        const updatedLeague = await leagueService.updateLeagueCode(league.id, leagueCode.id);

        const leagueDto: LeagueDto = {
            id: updatedLeague.id,
            name: updatedLeague.name,
            owner_id: updatedLeague.owner_id,
            code: leagueCode.code
        };
        return res.status(200).json(leagueDto);
    } catch (error) {
        return res.status(400).json({error: "Error generating new league code"});
    }
};

export const leaveLeague = async (req: Request, res: Response) => {
    const userId = req.authUser.id;
    const { leagueId } = req.body;
    if (!validate([leagueId], ["number"], res)) return;

    try {
        const league = await leagueService.getLeagueById(leagueId);

        if (!league) return res.status(400).json({error: "No league found"});

        if (league.owner_id === userId) return res.status(400).json({error: "You can't leave your own league"});

        const leagueUser = await leagueService.deleteLeagueUser(userId, leagueId);

        res.status(200).json(leagueUser);
    } catch (error) {
        return res.status(400).json({error: "Error leaving league"});
    }
};

export const deleteLeague = async (req: Request, res: Response) => {
    const userId = req.authUser.id;
    const { id } = req.params;

    try {
        const leagueId = parseInt(id);
        const league = await leagueService.getLeagueById(leagueId);

        if (!league) return res.status(400).json({error: "No league found"});

        if (userId !== league.owner_id) return res.status(401).json({error: "League can only be deleted by its owner"});

        await leagueService.deleteLeague(leagueId);

        res.status(200).json(league);
    } catch (error) {
        return res.status(400).json({error: "Error deleting league"});
    }
};

export * as leagueController from "./league.controller";