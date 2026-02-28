#!/bin/bash

ASSETS_DIR="../app/src/main/assets"
mkdir -p $ASSETS_DIR
FILE="$ASSETS_DIR/course_data.json"

echo "[" > "$FILE"

# Генерируем 18 лунок
for hole_idx in {1..18}
do
    awk -v seed=$RANDOM -v h_num=$hole_idx 'BEGIN {
        srand(seed);
        
        # Случайная точка старта (Middle грина)
        mid_lat = 55.0 + (rand() * 5.0);
        mid_lon = 37.0 + (rand() * 5.0);
        
        # Параметры лунки
        par = (rand() > 0.7) ? 5 : ((rand() < 0.2) ? 3 : 4);
        offset = 0.00015 + (rand() * 0.0001);
        
        printf "  {\n"
        printf "    \"holeNumber\": %d,\n", h_num
        printf "    \"par\": %d,\n", par
        printf "    \"green\": {\n"
        printf "      \"front\":  {\"lat\": %.7f, \"lon\": %.7f},\n", mid_lat - offset, mid_lon
        printf "      \"middle\": {\"lat\": %.7f, \"lon\": %.7f},\n", mid_lat, mid_lon
        printf "      \"back\":   {\"lat\": %.7f, \"lon\": %.7f}\n", mid_lat + offset, mid_lon
        printf "    },\n"
        printf "    \"shots\": [\n"
        
        # Точка первого удара (далеко)
        curr_lat = mid_lat - (0.003 + rand() * 0.002);
        curr_lon = mid_lon - (0.003 + rand() * 0.002);
        
        # Случайное кол-во ударов от 2 до 6
        total_shots = 2 + int(rand() * 4);
        
        for (i = 1; i <= total_shots; i++) {
            printf "      {\"lat\": %.7f, \"lon\": %.7f}%s\n", curr_lat, curr_lon, (i == total_shots ? "" : ",")
            
            # Движение к цели
            dist_lat = mid_lat - curr_lat;
            dist_lon = mid_lon - curr_lon;
            curr_lat += (dist_lat * (0.5 + rand() * 0.4)) + ((rand()-0.5) * 0.0001);
            curr_lon += (dist_lon * (0.5 + rand() * 0.4)) + ((rand()-0.5) * 0.0001);
        }
        
        printf "    ]\n"
        printf "  }%s\n", (h_num == 18 ? "" : ",")
    }' >> "$FILE"
done

echo "]" >> "$FILE"
echo "Сгенерировано 18 лунок в $FILE"