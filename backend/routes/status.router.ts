import { Router } from "express";
import { matchController } from "../controllers/match.controller";

export const statusRouter = Router();

statusRouter.get("/season", matchController.getCurrentSeason);

statusRouter.get("/gameweek", matchController.getCurrentGameweek);