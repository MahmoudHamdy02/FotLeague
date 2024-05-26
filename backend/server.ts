import app from "./app";


// Start server
app.listen(3000, () => {
    console.log(`Listening on port ${process.env.PORT}`);
});