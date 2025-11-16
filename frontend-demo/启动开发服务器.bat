@echo off
chcp 65001 >nul
echo ========================================
echo 医疗器械标准数据库系统 - 前端演示
echo ========================================
echo.
echo 正在启动开发服务器...
echo.

cd /d "%~dp0"

if not exist "node_modules" (
    echo [提示] 首次运行，正在安装依赖...
    echo.
    call npm install
    echo.
)

echo [启动] 开发服务器启动中...
echo [提示] 浏览器将自动打开 http://localhost:3000
echo [提示] 按 Ctrl+C 可停止服务器
echo.

npm run dev

pause
