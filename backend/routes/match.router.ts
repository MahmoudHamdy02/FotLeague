import { Router } from "express";
import { matchController } from "../controllers/match.controller";
import { validate } from "../validators/validate";
import { matchValidator } from "../validators/match.validator";
import { validator } from "../validators/validator";

export const matchRouter = Router();

matchRouter.get("/:season", validator.season, validate, matchController.getMatchesBySeason);

matchRouter.post("/init", matchValidator.initializeMatches, validate, matchController.initializeMatches);

matchRouter.post("/update", matchValidator.updateMatch, validate, matchController.updateMatch);