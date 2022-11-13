@echo off
echo "DOCS PUSH BAT"

echo "2. git pull"
git pull

echo "3. git add *"
git add *

set now=%date% %time%
git commit -m "%now%"
echo "4. git commit -m "%now%""
 
echo "5. git push"
git push
 
echo "Batch execution complete!"
pause
