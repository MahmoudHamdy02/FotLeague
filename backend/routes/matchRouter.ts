import { Router } from "express";
import * as matchController from "../controllers/matchController";

const router = Router();

router.post("/init", matchController.initializeMatches);

export default router;