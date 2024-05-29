import { Router } from "express";
import * as matchController from "../controllers/matchController";

const router = Router();

router.get("/season/current", matchController.getCurrentSeason);

router.get("/:season", matchController.getMatchesBySeason);

router.post("/init", matchController.initializeMatches);

export default router;