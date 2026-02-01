/** @type {import('tailwindcss').Config} */
module.exports = {
    content: [
        "./src/**/*.{html,ts}",
    ],
    theme: {
        extend: {
            colors: {
                primary: {
                    DEFAULT: 'var(--ion-color-primary, #2563eb)',
                    shade: 'var(--ion-color-primary-shade, #1d4ed8)',
                    tint: 'var(--ion-color-primary-tint, #3b82f6)',
                    light: 'var(--ion-color-primary-light, #eff6ff)',
                },
                app: {
                    bg: 'var(--ion-background-color, #ffffff)',
                    text: 'var(--ion-text-color, #111827)',
                }
            },
            fontFamily: {
                app: ['var(--app-font-family)', 'Inter', 'sans-serif'],
            }
        },
    },
    plugins: [],
}
