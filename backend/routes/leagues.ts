import { Router } from "express";
import { requireAuth } from "../middleware/requireAuth";
import leagueService from "../services/leagueService";

const router = Router();

router.post("/", requireAuth, async (req, res) => {
    const userId = req.authUser.id;
    const { name } = req.body;
    try {
        const league = await leagueService.createLeague(name, userId);
        res.status(201).json(league);
    } catch (e) {
        console.log(e);
        res.status(400).json({error: "Error creating league"});
    }
});

export default router;