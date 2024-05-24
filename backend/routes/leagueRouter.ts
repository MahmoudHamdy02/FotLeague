import { Router } from "express";
import { requireAuth } from "../middleware/requireAuth";
import leagueController from "../controllers/leagueController";

const router = Router();

router.post("/", requireAuth, leagueController.createLeague);

export default router;