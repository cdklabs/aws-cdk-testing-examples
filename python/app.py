#!/usr/bin/env python3
import os

from aws_cdk import core as cdk


app = cdk.App()

# Stacks are intentionally not created here -- this application isn't meant to
# be deployed.

app.synth()
