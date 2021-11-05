#!/usr/bin/env node
import "source-map-support/register";
import * as cdk from "@aws-cdk/core";

const app = new cdk.App();

// Stacks are intentionally not created here -- this application isn't meant to
// be deployed.
