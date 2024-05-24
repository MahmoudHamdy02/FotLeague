import { Request, Response } from "express";
import leagueService from "../services/leagueService";

namespace leagueController {
    // Utils
    const generateLeagueCode = (length: number): string => {
        let result = "";
        const characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        const charactersLength = characters.length;
        for (let counter = 0; counter < length; counter++) {
            result += characters.charAt(Math.floor(Math.random() * charactersLength));
        }
        return result;
    };

    const checkValidCode = async (code: string): Promise<boolean> => {
        // Check if a league already exists with this code
        const league = await leagueService.getLeagueByCode(code);
        return league === null;
    };

    // Route handlers
    export const createLeague = async (req: Request, res: Response) => {
        const userId = req.authUser.id;
        const { name } = req.body;

        // Generate random code
        let code = generateLeagueCode(6);
        console.log(code);
        let isValid = await checkValidCode(code);
        console.log(isValid);
        // Keep generating codes until we find a valid one
        while (!isValid) {
            code = generateLeagueCode(6);
            isValid = await checkValidCode(code);
        }

        try {
            const league = await leagueService.createLeague(name, userId, code);
            res.status(201).json(league);
        } catch (e) {
            console.log(e);
            res.status(400).json({error: "Error creating league"});
        }
    };
}

export default leagueController;