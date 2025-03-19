import { Router } from "express";
import { requireAuth } from "../middleware/requireAuth";
import { leagueController } from "../controllers/league.controller";
import { validator } from "../validators/validator";
import { validate } from "../validators/validate";
import { leagueValidator } from "../validators/league.validator";

export const leagueRouter = Router();

leagueRouter.get("/:id", requireAuth, validator.id, validate, leagueController.getLeagueDetailsById);

leagueRouter.post("/", requireAuth, leagueValidator.createLeague, validate, leagueController.createLeague);

leagueRouter.post("/join/", requireAuth, leagueValidator.joinLeague, validate, leagueController.joinLeague);

leagueRouter.get("/user/leagues", requireAuth, leagueController.getUserLeagues);

leagueRouter.post("/rename", requireAuth, leagueValidator.renameLeague, validate, leagueController.renameLeague);

leagueRouter.post("/generate", requireAuth, leagueValidator.generateNewLeagueCode, validate, leagueController.generateNewLeagueCode);

leagueRouter.post("/leave", requireAuth, leagueValidator.leaveLeague, validate, leagueController.leaveLeague);

leagueRouter.delete("/:id", requireAuth, validator.id, validate, leagueController.deleteLeague);
