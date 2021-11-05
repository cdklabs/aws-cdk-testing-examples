import setuptools

with open("README.md") as fp:
    long_description = fp.read()


setuptools.setup(
    name="app",
    version="0.0.1",
    license="MIT-0",

    description="An example CDK app to demonstrate the assertions module",
    long_description=long_description,
    long_description_content_type="text/markdown",

    author="AWS",

    package_dir={"": "app"},
    packages=setuptools.find_packages(where="app", exclude=("test",)),

    install_requires=[
        "aws-cdk.core==1.130.0",
        "aws-cdk.assertions==1.130.0",
        "aws-cdk.aws-cloudwatch==1.130.0",
        "aws-cdk.aws-lambda==1.130.0",
        "aws-cdk.aws-sns==1.130.0",
        "aws-cdk.aws-sns-subscriptions==1.130.0",
        "aws-cdk.aws-sqs==1.130.0",
        "aws-cdk.aws-stepfunctions==1.130.0",
    ],

    extras_require={
        "test": ["pytest", "syrupy"]
    },

    python_requires=">=3.6",

    classifiers=[
        "Development Status :: 4 - Beta",

        "Intended Audience :: Developers",

        "Programming Language :: JavaScript",
        "Programming Language :: Python :: 3 :: Only",
        "Programming Language :: Python :: 3.6",
        "Programming Language :: Python :: 3.7",
        "Programming Language :: Python :: 3.8",

        "Topic :: Software Development :: Code Generators",
        "Topic :: Utilities",

        "Typing :: Typed",
    ],
)
