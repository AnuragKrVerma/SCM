console.log("Script loaded");

// Change theme work
let currentTheme = getTheme();

// Initial theme setup
document.addEventListener("DOMContentLoaded", () => {
  changeTheme();
});

// Change theme function
function changeTheme() {
  // Set the initial theme to the web page
  changePageTheme(currentTheme);

  // Set the listener to change theme button
  const changeThemeButton = document.querySelector("#theme_change_button");

  changeThemeButton.addEventListener("click", () => {
    console.log("Change theme button clicked");
    currentTheme = currentTheme === "dark" ? "light" : "dark";
    changePageTheme(currentTheme);
  });
}

// Set theme to localStorage
function setTheme(theme) {
  localStorage.setItem("theme", theme);
}

// Get theme from localStorage
function getTheme() {
  const theme = localStorage.getItem("theme");
  return theme ? theme : "light";
}

// Change current page theme
function changePageTheme(theme) {
  // Update localStorage
  setTheme(theme);

  // Update the theme class on the HTML element
  const htmlElement = document.querySelector("html");
  htmlElement.classList.remove("dark", "light");
  htmlElement.classList.add(theme);

  // Change the text of the button
  const buttonSpan = document.querySelector("#theme_change_button span");
  buttonSpan.textContent = theme === "light" ? "Dark" : "Light";
}
