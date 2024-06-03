import { Router } from "express";
import { matchController } from "../controllers/match.controller";

export const matchRouter = Router();

matchRouter.get("/season/current", matchController.getCurrentSeason);

matchRouter.get("/:season", matchController.getMatchesBySeason);

matchRouter.post("/init", matchController.initializeMatches);
