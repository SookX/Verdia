/** @type {import('tailwindcss').Config} */
module.exports = {
  // NOTE: Update this to include the paths to all files that contain Nativewind classes.
  content: ["./app/**/*.{js,jsx,ts,tsx}", "./components/**/*.{js,jsx,ts,tsx}"],
  presets: [require("nativewind/preset")],
  theme: {
    extend: {
      colors: {
        primary: {
          100: "#40723d",
          400: "#799c77",
          500: "#8caa8b",
          600: "#a0b99e"
        },
        secondary: {
          100: "#124328",
          500: "#718e7e"
        },
        light: {
          100: "#ecf1ec",
          200: "#d9e3d8"
        },
        dark: {
          100: "#40723d",
          200: "#3a6737",
          300: "#335b31",
          400: "#2d502b"
        },
        neutral: {
          100: "#4d4d4d",
          200: "#666666",
          300: "#808080",
          400: "#999999",
          500: "#b3b3b3",
          600: "#cccccc",
          700: "#e6e6e6"
        }
      }
    },
  },
  plugins: [],
}