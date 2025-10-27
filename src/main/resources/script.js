document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('login-form');

    loginForm.addEventListener('submit', (event) => {
        // Prevent the default form submission (which reloads the page)
        event.preventDefault();

        // Get the values from the input fields
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;

        // For now, we'll just log them to the console for testing
        console.log('Login attempt with:');
        console.log('Username:', username);
        console.log('Password:', password);

        // Later, you will send this data to your Java backend here!
        alert(Login attempt for user: ${username});
    });
});