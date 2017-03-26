/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equitable_clustering_gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.PriorityQueue;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

/**
 *
 * @author aditya
 */
public class Equitable_Clustering_GUI{

    public static JFrame main_window;
    public static JButton button_render;
    public static JButton button_KMeans;
    public static JButton button_equitable;
    public static JButton button_priority;
    public static JButton button_clear;
    public static JTextField text_input = new JTextField(20);
    public static JPanel panel;
    public static Color[] myColors = {Color.BLUE,Color.GREEN,Color.RED,Color.YELLOW,Color.ORANGE,Color.BLACK,Color.CYAN,Color.LIGHT_GRAY,Color.PINK,Color.MAGENTA,Color.DARK_GRAY,Color.GRAY};
    
    static ArrayList<Point> dataPoints;
    static ArrayList<Point> new_centroids;
    static ArrayList<Point> old_centroids;
    static ArrayList<Integer> displacement;
    static ArrayList<ArrayList<Point>> clusters;
    static ArrayList<Graphics> dots;
    static int K = 0;
    static int cluster_priority_limit = 1;
    static int cluster_size_limit = 0;
    static int color_counter = 0;
    
    static String default_input = "1000 250 500\n" +
"608 156 1\n" +
"125 233 2\n" +
"172 175 2\n" +
"512 62 2\n" +
"659 40 2\n" +
"412 97 2\n" +
"664 354 3\n" +
"176 10 2\n" +
"320 8 1\n" +
"139 180 3\n" +
"341 297 2\n" +
"410 167 2\n" +
"178 322 2\n" +
"123 248 1\n" +
"136 251 3\n" +
"17 260 1\n" +
"52 28 1\n" +
"175 184 3\n" +
"16 57 3\n" +
"471 237 2\n" +
"147 166 1\n" +
"686 202 2\n" +
"542 72 1\n" +
"301 86 3\n" +
"273 340 2\n" +
"153 182 1\n" +
"174 126 3\n" +
"184 203 2\n" +
"575 247 3\n" +
"248 287 3\n" +
"428 123 3\n" +
"47 309 2\n" +
"335 307 1\n" +
"76 193 2\n" +
"151 247 1\n" +
"297 187 1\n" +
"298 196 1\n" +
"302 318 1\n" +
"341 289 2\n" +
"702 51 2\n" +
"364 226 1\n" +
"536 357 2\n" +
"128 303 2\n" +
"522 235 3\n" +
"408 164 3\n" +
"538 144 3\n" +
"92 272 3\n" +
"549 304 1\n" +
"129 170 2\n" +
"361 122 2\n" +
"606 71 3\n" +
"502 331 1\n" +
"280 19 1\n" +
"363 171 3\n" +
"138 346 1\n" +
"520 347 1\n" +
"383 109 1\n" +
"402 193 3\n" +
"693 48 2\n" +
"327 99 2\n" +
"318 19 3\n" +
"219 187 1\n" +
"322 73 3\n" +
"322 9 2\n" +
"194 178 2\n" +
"344 180 3\n" +
"227 278 3\n" +
"689 8 1\n" +
"18 36 1\n" +
"274 169 1\n" +
"619 329 2\n" +
"665 291 3\n" +
"81 278 2\n" +
"704 224 2\n" +
"348 112 2\n" +
"59 14 3\n" +
"341 46 3\n" +
"164 351 3\n" +
"111 34 1\n" +
"462 203 2\n" +
"270 92 3\n" +
"533 149 3\n" +
"616 291 3\n" +
"506 118 1\n" +
"322 154 2\n" +
"124 56 2\n" +
"589 4 2\n" +
"159 294 2\n" +
"556 238 2\n" +
"396 283 1\n" +
"693 355 2\n" +
"259 144 2\n" +
"429 205 3\n" +
"180 291 1\n" +
"154 354 3\n" +
"427 26 3\n" +
"695 227 3\n" +
"683 102 2\n" +
"601 299 3\n" +
"193 81 1\n" +
"179 53 2\n" +
"631 360 3\n" +
"206 32 1\n" +
"282 223 3\n" +
"129 264 1\n" +
"16 250 1\n" +
"213 136 3\n" +
"112 165 1\n" +
"114 234 3\n" +
"560 240 3\n" +
"647 325 2\n" +
"699 160 2\n" +
"129 271 3\n" +
"303 14 2\n" +
"417 131 2\n" +
"381 212 1\n" +
"527 15 1\n" +
"52 63 2\n" +
"567 90 1\n" +
"525 234 3\n" +
"683 51 1\n" +
"172 155 1\n" +
"29 180 3\n" +
"388 239 2\n" +
"386 128 2\n" +
"264 203 2\n" +
"301 106 3\n" +
"98 91 2\n" +
"196 2 1\n" +
"135 28 1\n" +
"400 152 2\n" +
"104 146 1\n" +
"123 298 1\n" +
"145 141 2\n" +
"610 321 3\n" +
"132 51 3\n" +
"17 139 3\n" +
"502 342 1\n" +
"194 84 3\n" +
"283 90 3\n" +
"550 134 3\n" +
"671 302 2\n" +
"175 285 2\n" +
"8 260 1\n" +
"432 31 3\n" +
"423 160 1\n" +
"562 200 3\n" +
"185 247 1\n" +
"679 239 3\n" +
"264 298 3\n" +
"484 296 3\n" +
"622 92 1\n" +
"479 256 3\n" +
"440 162 3\n" +
"267 350 1\n" +
"117 186 3\n" +
"497 232 2\n" +
"623 79 2\n" +
"285 229 2\n" +
"360 200 3\n" +
"7 85 1\n" +
"402 129 1\n" +
"646 169 3\n" +
"299 159 2\n" +
"373 351 3\n" +
"117 118 1\n" +
"372 343 2\n" +
"191 354 3\n" +
"117 140 2\n" +
"587 224 1\n" +
"300 119 2\n" +
"230 147 1\n" +
"86 165 1\n" +
"528 7 3\n" +
"299 126 2\n" +
"405 307 2\n" +
"125 247 3\n" +
"165 23 1\n" +
"598 216 2\n" +
"550 125 1\n" +
"485 141 1\n" +
"583 207 3\n" +
"276 219 1\n" +
"450 248 1\n" +
"653 185 2\n" +
"417 199 3\n" +
"236 134 1\n" +
"115 159 1\n" +
"281 97 1\n" +
"84 219 1\n" +
"480 287 1\n" +
"112 223 1\n" +
"322 246 2\n" +
"653 226 2\n" +
"648 67 2\n" +
"411 69 1\n" +
"266 9 1\n" +
"471 269 3\n" +
"502 168 3\n" +
"136 337 2\n" +
"673 263 3\n" +
"280 149 3\n" +
"306 303 2\n" +
"478 339 3\n" +
"201 204 3\n" +
"16 165 2\n" +
"239 52 1\n" +
"195 80 2\n" +
"173 348 1\n" +
"85 48 3\n" +
"355 303 3\n" +
"226 57 3\n" +
"681 61 1\n" +
"586 232 2\n" +
"80 93 1\n" +
"638 337 1\n" +
"510 233 3\n" +
"72 83 3\n" +
"359 247 2\n" +
"418 57 2\n" +
"146 96 1\n" +
"483 275 3\n" +
"240 4 3\n" +
"506 42 2\n" +
"390 330 1\n" +
"225 34 3\n" +
"617 354 1\n" +
"169 174 3\n" +
"28 11 2\n" +
"530 223 3\n" +
"334 255 1\n" +
"49 171 3\n" +
"520 312 2\n" +
"427 336 3\n" +
"102 45 1\n" +
"22 278 2\n" +
"291 238 1\n" +
"654 149 3\n" +
"651 139 3\n" +
"613 104 2\n" +
"240 170 3\n" +
"415 94 2\n" +
"54 264 3\n" +
"659 319 3\n" +
"56 339 2\n" +
"56 114 1\n" +
"283 27 1\n" +
"96 316 3\n" +
"308 333 2\n" +
"83 68 2\n" +
"497 214 1\n" +
"505 124 3\n" +
"288 120 2\n" +
"129 219 1\n" +
"195 352 2\n" +
"207 54 3\n" +
"143 253 3\n" +
"445 301 2\n" +
"191 297 3\n" +
"332 336 3\n" +
"691 193 3\n" +
"263 88 3\n" +
"159 359 3\n" +
"304 126 3\n" +
"348 210 1\n" +
"286 54 2\n" +
"158 183 3\n" +
"73 184 2\n" +
"289 223 1\n" +
"616 203 1\n" +
"392 343 3\n" +
"303 23 3\n" +
"426 125 1\n" +
"487 334 2\n" +
"222 48 2\n" +
"566 260 3\n" +
"581 292 3\n" +
"266 9 3\n" +
"605 201 2\n" +
"695 204 2\n" +
"580 305 2\n" +
"348 357 3\n" +
"34 294 2\n" +
"478 109 2\n" +
"240 229 1\n" +
"670 310 3\n" +
"388 135 3\n" +
"110 190 3\n" +
"177 179 1\n" +
"388 352 1\n" +
"655 276 1\n" +
"547 70 3\n" +
"125 127 3\n" +
"117 12 2\n" +
"213 182 3\n" +
"597 4 3\n" +
"496 211 2\n" +
"291 290 2\n" +
"594 185 1\n" +
"146 85 1\n" +
"118 307 2\n" +
"94 179 1\n" +
"421 237 1\n" +
"617 8 3\n" +
"675 153 2\n" +
"269 259 3\n" +
"21 107 1\n" +
"155 34 2\n" +
"460 299 2\n" +
"100 241 1\n" +
"14 203 1\n" +
"272 295 1\n" +
"54 49 2\n" +
"241 123 1\n" +
"630 108 3\n" +
"703 134 3\n" +
"270 347 3\n" +
"569 122 3\n" +
"568 189 3\n" +
"651 271 1\n" +
"492 72 1\n" +
"36 70 1\n" +
"648 33 3\n" +
"665 116 3\n" +
"361 10 2\n" +
"261 95 3\n" +
"396 63 3\n" +
"650 337 3\n" +
"371 37 3\n" +
"404 304 3\n" +
"340 342 1\n" +
"522 298 2\n" +
"263 183 3\n" +
"373 281 1\n" +
"377 31 1\n" +
"360 64 2\n" +
"547 225 1\n" +
"617 201 2\n" +
"528 244 1\n" +
"82 87 1\n" +
"94 212 1\n" +
"517 101 2\n" +
"266 86 2\n" +
"405 63 2\n" +
"362 328 3\n" +
"656 321 3\n" +
"264 179 3\n" +
"557 69 1\n" +
"201 135 3\n" +
"527 65 2\n" +
"469 246 1\n" +
"602 301 3\n" +
"684 351 3\n" +
"99 140 3\n" +
"575 265 2\n" +
"275 142 1\n" +
"564 53 2\n" +
"574 108 3\n" +
"352 78 1\n" +
"77 34 2\n" +
"46 204 2\n" +
"399 213 3\n" +
"270 298 1\n" +
"565 226 1\n" +
"603 6 1\n" +
"265 185 3\n" +
"258 298 3\n" +
"107 298 2\n" +
"558 98 3\n" +
"619 101 3\n" +
"382 167 3\n" +
"690 326 3\n" +
"98 260 1\n" +
"453 275 2\n" +
"85 12 3\n" +
"625 272 1\n" +
"108 3 3\n" +
"367 268 3\n" +
"688 120 3\n" +
"352 348 1\n" +
"541 12 1\n" +
"283 273 3\n" +
"661 327 3\n" +
"264 187 3\n" +
"84 69 2\n" +
"179 269 2\n" +
"176 282 3\n" +
"125 201 3\n" +
"124 332 1\n" +
"45 137 3\n" +
"13 262 3\n" +
"209 219 2\n" +
"366 118 3\n" +
"542 204 2\n" +
"52 155 2\n" +
"375 216 1\n" +
"27 244 1\n" +
"233 327 2\n" +
"483 354 3\n" +
"202 251 3\n" +
"687 232 1\n" +
"444 2 2\n" +
"24 193 3\n" +
"60 299 1\n" +
"97 22 3\n" +
"269 6 2\n" +
"516 210 3\n" +
"540 311 2\n" +
"311 334 3\n" +
"172 303 3\n" +
"24 255 3\n" +
"531 265 3\n" +
"639 278 2\n" +
"154 156 2\n" +
"674 18 1\n" +
"512 206 3\n" +
"266 54 3\n" +
"167 149 2\n" +
"203 344 2\n" +
"550 127 3\n" +
"121 139 2\n" +
"661 73 2\n" +
"76 189 3\n" +
"583 320 1\n" +
"637 21 1\n" +
"187 210 3\n" +
"615 10 2\n" +
"307 226 1\n" +
"213 85 1\n" +
"133 119 1\n" +
"677 331 2\n" +
"294 351 3\n" +
"152 231 1\n" +
"606 37 2\n" +
"226 5 1\n" +
"680 210 1\n" +
"222 102 3\n" +
"73 289 1\n" +
"306 141 1\n" +
"191 184 1\n" +
"553 253 3\n" +
"105 94 2\n" +
"526 11 3\n" +
"68 183 3\n" +
"542 306 3\n" +
"495 287 2\n" +
"498 228 1\n" +
"522 10 1\n" +
"492 197 2\n" +
"38 121 3\n" +
"543 358 2\n" +
"668 246 1\n" +
"313 79 1\n" +
"267 79 3\n" +
"628 134 2\n" +
"345 268 3\n" +
"436 359 1\n" +
"236 34 1\n" +
"15 286 2\n" +
"22 44 2\n" +
"520 166 1\n" +
"202 60 2\n" +
"214 218 2\n" +
"550 314 3\n" +
"491 306 3\n" +
"474 193 2\n" +
"688 216 2\n" +
"622 47 3\n" +
"526 25 1\n" +
"692 266 3\n" +
"178 95 3\n" +
"84 103 2\n" +
"469 356 2\n" +
"660 70 2\n" +
"527 160 1\n" +
"645 291 2\n" +
"222 299 2\n" +
"207 142 3\n" +
"604 121 2\n" +
"451 210 2\n" +
"72 96 3\n" +
"246 276 2\n" +
"197 33 3\n" +
"49 145 3\n" +
"367 19 1\n" +
"294 329 1\n" +
"409 77 3\n" +
"313 292 1\n" +
"151 110 2\n" +
"411 73 3\n" +
"344 224 3\n" +
"696 304 3\n" +
"257 218 1\n" +
"8 36 2\n" +
"284 265 2\n" +
"94 263 2\n" +
"334 135 3\n" +
"192 250 2\n" +
"537 54 2\n" +
"190 271 2\n" +
"116 9 2\n" +
"191 299 2\n" +
"274 238 1\n" +
"445 311 2\n" +
"130 22 3\n" +
"64 34 2\n" +
"418 339 1\n" +
"610 63 3\n" +
"192 325 1\n" +
"622 277 3\n" +
"192 334 2\n" +
"506 35 2\n" +
"228 289 1\n" +
"42 323 2\n" +
"184 140 2\n" +
"674 76 3\n" +
"636 68 1\n" +
"387 57 1\n" +
"279 270 3\n" +
"607 348 1\n" +
"70 174 3\n" +
"422 145 3\n" +
"125 207 1\n" +
"5 315 2\n" +
"383 181 3\n" +
"226 33 2\n" +
"349 245 3\n" +
"347 3 1\n" +
"355 88 1\n" +
"349 101 3\n" +
"689 138 3\n" +
"39 180 3\n" +
"140 100 2\n" +
"5 9 2\n" +
"390 132 3\n" +
"202 309 2\n" +
"513 120 3\n" +
"700 166 2\n" +
"236 312 3\n" +
"469 288 2\n" +
"396 141 3\n" +
"63 344 3\n" +
"271 317 1\n" +
"503 105 3\n" +
"61 85 2\n" +
"206 281 1\n" +
"117 140 1\n" +
"15 235 1\n" +
"650 180 1\n" +
"81 84 2\n" +
"258 333 2\n" +
"333 223 3\n" +
"277 301 1\n" +
"24 129 2\n" +
"399 245 3\n" +
"41 298 3\n" +
"43 68 3\n" +
"221 88 2\n" +
"691 27 2\n" +
"15 161 3\n" +
"431 274 1\n" +
"103 29 3\n" +
"78 73 1\n" +
"406 76 2\n" +
"499 142 3\n" +
"594 88 1\n" +
"568 47 1\n" +
"634 108 2\n" +
"83 94 2\n" +
"518 335 1\n" +
"57 114 1\n" +
"25 11 3\n" +
"432 245 1\n" +
"698 18 1\n" +
"143 244 1\n" +
"286 231 3\n" +
"63 336 2\n" +
"450 34 3\n" +
"48 133 2\n" +
"435 51 2\n" +
"45 177 1\n" +
"281 230 3\n" +
"135 198 1\n" +
"401 106 1\n" +
"339 189 2\n" +
"105 298 2\n" +
"645 198 3\n" +
"635 248 2\n" +
"19 130 3\n" +
"28 29 1\n" +
"233 45 1\n" +
"256 358 2\n" +
"534 280 1\n" +
"423 295 3\n" +
"70 174 1\n" +
"147 354 3\n" +
"556 137 1\n" +
"508 196 3\n" +
"657 353 3\n" +
"513 42 2\n" +
"498 124 3\n" +
"209 225 1\n" +
"484 134 1\n" +
"129 19 1\n" +
"674 235 3\n" +
"676 321 3\n" +
"682 173 3\n" +
"24 274 3\n" +
"298 13 2\n" +
"38 307 2\n" +
"484 282 3\n" +
"499 30 2\n" +
"144 103 2\n" +
"195 67 2\n" +
"248 45 3\n" +
"618 128 2\n" +
"294 131 1\n" +
"360 146 3\n" +
"56 55 3\n" +
"624 9 2\n" +
"506 35 3\n" +
"520 293 3\n" +
"172 249 3\n" +
"574 166 2\n" +
"364 224 3\n" +
"518 190 2\n" +
"110 229 3\n" +
"110 50 3\n" +
"579 331 3\n" +
"360 305 1\n" +
"614 296 3\n" +
"499 208 3\n" +
"559 141 3\n" +
"217 247 1\n" +
"198 127 3\n" +
"494 284 2\n" +
"302 211 1\n" +
"441 32 1\n" +
"183 357 2\n" +
"152 62 3\n" +
"326 209 3\n" +
"173 335 3\n" +
"682 159 3\n" +
"11 42 2\n" +
"203 128 1\n" +
"610 279 3\n" +
"123 310 3\n" +
"284 294 1\n" +
"232 275 3\n" +
"211 341 2\n" +
"336 243 3\n" +
"164 337 1\n" +
"193 195 1\n" +
"576 330 3\n" +
"518 217 1\n" +
"687 180 2\n" +
"533 236 2\n" +
"682 22 2\n" +
"117 332 2\n" +
"446 160 1\n" +
"29 231 1\n" +
"581 59 2\n" +
"318 94 3\n" +
"32 149 3\n" +
"186 54 3\n" +
"553 53 2\n" +
"655 122 3\n" +
"220 106 1\n" +
"279 175 2\n" +
"176 58 3\n" +
"570 25 1\n" +
"66 182 2\n" +
"487 192 1\n" +
"201 203 2\n" +
"554 249 2\n" +
"383 25 1\n" +
"691 26 3\n" +
"286 254 3\n" +
"591 149 1\n" +
"680 136 1\n" +
"579 217 3\n" +
"541 14 2\n" +
"153 279 3\n" +
"30 52 3\n" +
"695 335 2\n" +
"593 249 3\n" +
"345 227 2\n" +
"505 206 1\n" +
"569 153 1\n" +
"141 108 1\n" +
"592 4 3\n" +
"124 137 1\n" +
"378 305 3\n" +
"660 110 3\n" +
"593 201 2\n" +
"529 339 2\n" +
"335 228 1\n" +
"363 167 2\n" +
"178 74 3\n" +
"414 81 3\n" +
"205 101 1\n" +
"45 44 1\n" +
"551 251 1\n" +
"662 319 3\n" +
"89 54 2\n" +
"77 258 1\n" +
"151 51 2\n" +
"595 145 3\n" +
"531 32 3\n" +
"636 44 1\n" +
"416 319 3\n" +
"213 161 2\n" +
"481 24 1\n" +
"338 329 1\n" +
"370 247 1\n" +
"448 182 3\n" +
"509 282 3\n" +
"442 69 2\n" +
"619 225 3\n" +
"403 269 3\n" +
"295 10 3\n" +
"601 292 1\n" +
"379 76 3\n" +
"120 135 1\n" +
"121 286 1\n" +
"388 30 1\n" +
"423 172 2\n" +
"525 287 2\n" +
"46 208 1\n" +
"234 298 2\n" +
"376 188 2\n" +
"489 318 2\n" +
"261 272 2\n" +
"205 283 2\n" +
"186 273 3\n" +
"453 236 1\n" +
"488 359 3\n" +
"365 104 3\n" +
"666 42 2\n" +
"598 110 3\n" +
"683 78 1\n" +
"172 148 3\n" +
"398 134 1\n" +
"439 23 1\n" +
"423 93 1\n" +
"518 299 3\n" +
"324 206 1\n" +
"312 252 2\n" +
"134 136 1\n" +
"176 284 1\n" +
"610 105 2\n" +
"554 120 2\n" +
"561 325 2\n" +
"268 56 2\n" +
"685 237 2\n" +
"502 296 1\n" +
"318 353 1\n" +
"671 286 2\n" +
"696 138 1\n" +
"321 332 3\n" +
"290 337 1\n" +
"606 306 3\n" +
"33 27 3\n" +
"658 323 2\n" +
"222 133 2\n" +
"144 33 2\n" +
"475 358 3\n" +
"550 3 1\n" +
"351 278 3\n" +
"77 226 1\n" +
"207 58 1\n" +
"646 10 2\n" +
"500 49 2\n" +
"133 268 3\n" +
"333 167 2\n" +
"543 227 2\n" +
"366 252 3\n" +
"673 239 2\n" +
"190 120 2\n" +
"627 280 2\n" +
"578 222 3\n" +
"630 265 2\n" +
"593 133 2\n" +
"72 137 3\n" +
"6 44 3\n" +
"236 342 1\n" +
"67 30 2\n" +
"218 303 2\n" +
"497 358 2\n" +
"497 276 3\n" +
"525 145 3\n" +
"519 167 1\n" +
"573 289 3\n" +
"583 186 3\n" +
"380 252 2\n" +
"50 158 1\n" +
"465 294 1\n" +
"529 347 3\n" +
"479 245 1\n" +
"122 85 2\n" +
"384 241 1\n" +
"618 308 3\n" +
"189 294 2\n" +
"453 50 2\n" +
"693 156 3\n" +
"188 174 2\n" +
"562 94 1\n" +
"680 236 3\n" +
"460 145 1\n" +
"567 273 3\n" +
"393 50 1\n" +
"425 200 1\n" +
"70 47 1\n" +
"657 31 3\n" +
"196 27 1\n" +
"663 52 3\n" +
"277 356 2\n" +
"279 122 1\n" +
"21 113 1\n" +
"455 56 1\n" +
"311 218 3\n" +
"243 34 1\n" +
"700 84 3\n" +
"237 348 1\n" +
"561 269 1\n" +
"237 118 2\n" +
"646 10 1\n" +
"676 10 3\n" +
"345 121 2\n" +
"157 293 3\n" +
"109 172 3\n" +
"259 225 3\n" +
"597 82 1\n" +
"602 288 3\n" +
"113 203 3\n" +
"679 251 3\n" +
"599 307 1\n" +
"320 319 1\n" +
"603 303 1\n" +
"174 228 2\n" +
"522 216 3\n" +
"520 206 3\n" +
"45 168 2\n" +
"430 271 1\n" +
"585 143 3\n" +
"677 204 3\n" +
"616 318 2\n" +
"71 47 2\n" +
"167 121 3\n" +
"255 47 1\n" +
"230 346 3\n" +
"206 39 3\n" +
"320 160 1\n" +
"661 118 3\n" +
"31 167 3\n" +
"682 190 1\n" +
"560 65 3\n" +
"516 194 1\n" +
"326 10 2\n" +
"303 91 2\n" +
"422 9 3\n" +
"611 307 2\n" +
"697 229 3\n" +
"23 209 1\n" +
"163 85 1\n" +
"38 92 1\n" +
"339 180 3\n" +
"46 16 3\n" +
"105 266 2\n" +
"186 324 2\n" +
"250 71 2\n" +
"589 104 1\n" +
"251 356 2\n" +
"212 26 3\n" +
"489 197 1\n" +
"85 106 3\n" +
"163 2 3\n" +
"57 146 3\n" +
"335 73 3\n" +
"38 28 3\n" +
"324 95 3\n" +
"442 321 1\n" +
"375 198 1\n" +
"515 304 1\n" +
"126 36 3\n" +
"606 66 3\n" +
"549 245 2\n" +
"611 147 1\n" +
"348 185 3\n" +
"252 36 2\n" +
"194 351 1\n" +
"246 270 1\n" +
"509 123 3\n" +
"180 25 2\n" +
"187 230 1\n" +
"653 354 1\n" +
"14 56 3\n" +
"526 276 2\n" +
"154 94 1\n" +
"424 173 1\n" +
"108 342 2\n" +
"93 311 3\n" +
"486 288 3\n" +
"366 179 2\n" +
"5 154 1\n" +
"543 71 1\n" +
"27 134 3\n" +
"563 227 1\n" +
"277 229 1\n" +
"449 126 1\n" +
"75 282 3\n" +
"30 136 1\n" +
"21 241 2\n" +
"59 45 1\n" +
"148 280 1\n" +
"322 24 2\n" +
"484 266 2\n" +
"62 179 1\n" +
"422 60 2\n" +
"15 260 2\n" +
"634 137 1\n" +
"209 117 3\n" +
"624 230 3\n" +
"170 349 2\n" +
"193 122 1\n" +
"535 282 2\n" +
"591 146 2\n" +
"346 256 1\n" +
"470 61 2\n" +
"330 75 2\n" +
"403 44 1\n" +
"414 70 3\n" +
"538 66 3\n" +
"666 173 2\n" +
"15 191 3\n" +
"148 34 2\n" +
"703 222 3\n" +
"169 348 2\n" +
"72 86 3\n" +
"376 55 1\n" +
"23 123 2\n" +
"458 28 3\n" +
"178 196 1\n" +
"254 22 1\n" +
"692 229 2\n" +
"314 126 3\n" +
"380 61 3\n" +
"442 337 1\n" +
"7 247 3\n" +
"50 181 1\n" +
"306 183 3\n" +
"678 16 2\n" +
"470 204 1\n" +
"487 227 1\n" +
"60 323 3\n" +
"91 37 1\n" +
"359 330 2\n" +
"265 244 2\n" +
"145 253 2\n" +
"149 30 1\n" +
"307 137 1\n" +
"110 237 1\n" +
"586 170 1\n" +
"488 47 1\n" +
"482 186 1\n" +
"540 322 2\n" +
"17 209 1\n" +
"403 241 3\n" +
"697 220 2\n" +
"270 90 1\n" +
"275 34 1\n" +
"192 123 3\n" +
"644 352 3\n" +
"220 201 3\n" +
"138 106 3\n" +
"582 355 2\n" +
"42 61 1\n" +
"534 120 3\n" +
"128 25 3\n" +
"545 33 1\n" +
"213 222 2\n" +
"222 220 2\n" +
"229 106 1\n" +
"52 49 3\n" +
"265 7 3\n" +
"674 81 1\n" +
"539 329 3\n" +
"440 3 2\n" +
"334 279 3\n" +
"165 210 3\n" +
"258 283 3\n" +
"238 233 2\n" +
"565 48 2\n" +
"271 247 2\n" +
"486 94 1\n" +
"270 324 1\n" +
"153 67 3\n" +
"264 10 3\n" +
"161 224 2\n" +
"331 347 1";
    public static void main(String[] args) {
        
        buildMainWindow();
    }
    
    public static void buildMainWindow(){
        main_window = new JFrame();
        main_window.setTitle("AMI Clustering");
        main_window.setSize(480,320);
        main_window.setLocation(220,180);
        main_window.setResizable(false);
        main_window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        configureMainWindow();
        mainWindow_action();
        main_window.setVisible(true);
    }
    
    public static void configureMainWindow(){
        main_window.setBackground(new Color(255, 255, 255));
        main_window.setSize(720,485);
        main_window.getContentPane().setLayout(null);
      
        panel = new JPanel();
        panel.setSize(720, 485);
        panel.setBackground(Color.WHITE);
        //panel.setLayout(null);
        main_window.getContentPane().add(panel);
        
        button_render = new JButton();
        button_render.setBackground(new Color(0,0,255));
        button_render.setForeground(new Color(255,255,255));
        button_render.setText("Render");
        //main_window.getContentPane().add(button_render);
        panel.add(button_render);
        button_render.setBounds(5,50,120,25);
      
        button_KMeans = new JButton();
        button_KMeans.setBackground(new Color(0,0,255));
        button_KMeans.setForeground(new Color(255,255,255));
        button_KMeans.setText("K-Means");
        button_KMeans.setToolTipText("");
        //main_window.getContentPane().add(button_KMeans);
        panel.add(button_KMeans);
        button_KMeans.setBounds(170,50,120,25);
        button_KMeans.setEnabled(false);
        
        text_input.setForeground(new Color(0,0,255));
        //main_window.getContentPane().add(text_input);
        
        text_input.setBounds(5, 5, 510, 50);
        text_input.setText(default_input);
        panel.add(text_input);
    
        button_equitable = new JButton();
        button_equitable.setBackground(new Color(0,0,255));
        button_equitable.setForeground(new Color(255, 255, 255));
        button_equitable.setText("Equitable");
        //main_window.getContentPane().add(button_equitable);
        panel.add(button_equitable);
        button_equitable.setBounds(335,50,120,25);
        button_equitable.setEnabled(false);
        
        button_priority = new JButton();
        button_priority.setBackground(new Color(0,0,255));
        button_priority.setForeground(new Color(255, 255, 255));
        button_priority.setText("Priority");
        //main_window.getContentPane().add(button_equitable);
        panel.add(button_priority);
        button_priority.setBounds(335,50,120,25);
        button_priority.setEnabled(false);
        
        button_clear = new JButton();
        button_clear.setBackground(new Color(0,0,255));
        button_clear.setForeground(new Color(255, 255, 255));
        button_clear.setText("Clear");
        //main_window.getContentPane().add(button_equitable);
        panel.add(button_clear);
        button_clear.setBounds(335,50,120,25);
        button_clear.setEnabled(true);
        
        //main_window.pack();

    }
    
    public static void mainWindow_action(){
        button_render.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                action_button_render();
            }
        });
        
        button_KMeans.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("<-------------- K-Means Clustering -------------->\n");
                KMeansAlgo(K,dataPoints,old_centroids,new_centroids,displacement,clusters,true);
            }
        });
        
        button_equitable.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("<-------------- Equitable Shifting -------------->\n");
                makeEquitable();
            }
        });
        
        button_priority.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("<-------------- Priority Decomposition -------------->\n");
                for(int i=0;i<clusters.size();i++)
                    handlePriority(clusters.get(i));
                button_equitable.setEnabled(false);
                button_KMeans.setEnabled(false);
                button_priority.setEnabled(false);
                button_render.setEnabled(false);
            }
        });
        
        button_clear.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                panel.repaint();
                button_KMeans.setEnabled(false);
                button_equitable.setEnabled(false);
                button_priority.setEnabled(false);
                button_render.setEnabled(true);
                color_counter = 0;
            }
        });
        
    }
    
    public static void action_button_render(){
        String input = text_input.getText();
        String[] inp_arr = input.split(" ");
        int noOfPoints = Integer.parseInt(inp_arr[0]);
        cluster_size_limit = Integer.parseInt(inp_arr[1]);
        cluster_priority_limit = Integer.parseInt(inp_arr[2]);
        K = (int)Math.ceil(noOfPoints/(double)cluster_size_limit);
        System.out.println("No. of Smart Meters: "+noOfPoints+"\nSize Threshold: "+cluster_size_limit+"\nPriority Threshold: "+cluster_priority_limit+"\n");
        dataPoints = new ArrayList<>();
        new_centroids = new ArrayList<>();
        old_centroids = new ArrayList<>();
        displacement = new ArrayList<>();
        clusters = new ArrayList<>();
        dots = new ArrayList<>();
        
        int it = 3;
        
        for (int i = 0; i < K; i++) {
            clusters.add(new ArrayList<>());
        }
        
        for (int i = 0; i < noOfPoints; i++) {
            int x_coordinate = Integer.parseInt(inp_arr[it++]);
            int y_coordinate = Integer.parseInt(inp_arr[it++])+50;
            int point_priority = Integer.parseInt(inp_arr[it++]);
            Point point = new Point(x_coordinate,y_coordinate,point_priority);
            Graphics g = panel.getGraphics();
            g.setColor(Color.BLACK);
            if(point.priority == 1)
                g.fillOval(x_coordinate, y_coordinate, 8, 8);
            else if(point.priority == 2)
                g.fillRect(x_coordinate, y_coordinate, 7, 7); 
            else{
                int xpoints[] = {x_coordinate, x_coordinate-4, x_coordinate+3};
                int ypoints[] = {y_coordinate-4, y_coordinate+4, y_coordinate+3};
                int npoints = 3;
    
                g.fillPolygon(xpoints, ypoints, npoints);
            }
            g.dispose();
            dataPoints.add(point);
            if (i < K) 
                new_centroids.add(dataPoints.get(i));
        }
        button_KMeans.setEnabled(true);
    }
    
    public static void KMeansAlgo(int K, ArrayList<Point> dataPoints, ArrayList<Point> old_centroids, ArrayList<Point> new_centroids, ArrayList<Integer> displacement, ArrayList<ArrayList<Point>> clusters, boolean flag){
        
        int iteration_count = 1;
        boolean is_looping = true;
        
        
        do {         
            /*
            * Calculate euclidean distance of each point to each centroid
            * and allocate the point to the cluster having nearest centroid
            * Time Complexity: O(nk)
            */
            for (Point point : dataPoints) {
                for (Point c : new_centroids) {
                    int dist = (int)Math.sqrt((c.x - point.x)*(c.x - point.x)+(c.y - point.y)*(c.y - point.y));
                    displacement.add(dist);
                }
                int clusterID = displacement.indexOf(Collections.min(displacement));
                point.clusterID = clusterID;
                clusters.get(clusterID).add(point);
                displacement.removeAll(displacement);
                
            }
            
            /*
            * Update Old Centroids and calculate new centroids
            * Time Complexity: O(nk)
            */
            for (int i = 0; i < K; i++) {
                if (iteration_count == 1)
                    old_centroids.add(new_centroids.get(i));
                else 
                    old_centroids.set(i, new_centroids.get(i));
                if (!clusters.get(i).isEmpty()) 
                    new_centroids.set(i, getCentroid(clusters.get(i)));
            }
            
            is_looping = compareLists(K,old_centroids,new_centroids);
            
            if (is_looping) {
                for (ArrayList<Point> cluster : clusters) {
                    cluster.removeAll(cluster);
                }
            }
            iteration_count++;
        } while (is_looping);
        
        //System.out.println("\nX----- K - Means Clustering -----X\n");
        for (int i = 0; i < new_centroids.size(); i++) {
            //System.out.println("Original Centroid" + (i + 1) + " " + new_centroids.get(i));
        }
        //System.out.println("");
        for (int i = 0; i < clusters.size(); i++) {
            //System.out.println("\nOriginal Cluster " + (i + 1));
            //System.out.println(clusters.get(i).toString());
            int cluster_priority = 0;
            for(int j=0;j<clusters.get(i).size();j++){
                int x_c = clusters.get(i).get(j).x;
                int y_c = clusters.get(i).get(j).y;
                int priority = clusters.get(i).get(j).priority;
                cluster_priority+=priority;
                Graphics g = panel.getGraphics();
                g.setColor(myColors[i]);
                if(priority == 1)
                    g.fillOval(x_c, y_c, 8, 8);
                else if(priority == 2)
                    g.fillRect(x_c, y_c, 7, 7); 
                else{
                    int xpoints[] = {x_c, x_c-4, x_c+3};
                    int ypoints[] = {y_c-4, y_c+4, y_c+3};
                    int npoints = 3;
    
                    g.fillPolygon(xpoints, ypoints, npoints);
                }
                g.dispose();
            }
            if(flag)
                System.out.println("Cluster "+(i+1)+": Size - "+clusters.get(i).size()+"\tPriority - "+cluster_priority+"\n");
        }
        //System.out.println("Number of Iterations: " + iteration_count);
        button_KMeans.setEnabled(false);
        button_equitable.setEnabled(true);
    }
    
    /*
    * This method is Post-Processing step
    * changes the clusters obtained by
    * K-means algorithm so that each cluster
    * satisfies the threshold limit value
    */
    public static void makeEquitable(){
        DisplacementComparator comparator = new DisplacementComparator();
        PriorityQueue<Point> min_heap = new PriorityQueue<>(comparator);
        
        /*
        * Calculate euclidean distance of each point to each centroid
        * except its own and make two duplicate lists of these values
        * Time Complexity: O(nk)
        */
        for (Point point : dataPoints) {
            for(int i=0;i<K;i++){   
                if(i != point.clusterID){
                    point.id_others.add(i);
                    point.id_others_original.add(i);
                    point.dist_others.add((int)Math.sqrt((new_centroids.get(i).x - point.x)*(new_centroids.get(i).x - point.x)+(new_centroids.get(i).y - point.y)*(new_centroids.get(i).y - point.y)));
                    point.dist_others_original.add((int)Math.sqrt((new_centroids.get(i).x - point.x)*(new_centroids.get(i).x - point.x)+(new_centroids.get(i).y - point.y)*(new_centroids.get(i).y - point.y)));
                }
            }
        }
        HashSet<Integer> hset = new HashSet<>();
        // Max Iterations: k-1
        while(true){
            
            int max_ind = 0,max_size=0;
            
            /*
            * Get the cluster with maximum size
            * Time Complexity: O(k)
            */
            for(int i=0;i<K;i++){
                if(!hset.contains(i) && clusters.get(i).size() > max_size){
                    max_size = clusters.get(i).size();
                    max_ind = i;
                }
            }
            if(max_size <= cluster_size_limit)
                break;
            hset.add(max_ind);
            
            /*
            * Add each point of the cluster in Priority Queue
            * Time Complexity: O(nlogn)
            */
            for(Point point: clusters.get(max_ind)){
                min_heap.add(point);
            }
            
            
            while(!min_heap.isEmpty() && clusters.get(max_ind).size() > cluster_size_limit){
                Point point = min_heap.poll();
                //System.out.println(point.toString()+" "+point.dist_others.toString()+" "+point.id_others.toString());
                if(point.dist_others.isEmpty()){
                    point.restoreLists();
                    continue;
                }
                int min_index = point.dist_others.indexOf(Collections.min(point.dist_others));
                if(!hset.contains(point.id_others.get(min_index))){
                    clusters.get(point.clusterID).remove(point);
                    point.clusterID = point.id_others.get(min_index);
                    clusters.get(point.id_others.get(min_index)).add(point);
                    point.restoreLists();
                }
                else{
                    point.dist_others.remove(min_index);
                    point.id_others.remove(min_index);
                    min_heap.add(point);
                }
            }
            min_heap.clear();
        }
        
        /*
        * Recompute Final Centroids
        * Time Complexity: O(nk)
        */
        for (int i = 0; i < K; i++) {
            if (!clusters.get(i).isEmpty()) 
                new_centroids.set(i, getCentroid(clusters.get(i)));
        }
        
        //System.out.println("\nX----- Equitable Clustering -----X\n");
        for (int i = 0; i < new_centroids.size(); i++) {
            //System.out.println("New Centroid" + (i + 1) + " " + new_centroids.get(i));
        }
        
        //System.out.println("");
        
        for (int i = 0; i < clusters.size(); i++) {
            //System.out.println("\nNew Cluster " + (i + 1));
            //System.out.println(clusters.get(i).toString());
            int cluster_priority = 0;
            for(int j=0;j<clusters.get(i).size();j++){
                int x_c = clusters.get(i).get(j).x;
                int y_c = clusters.get(i).get(j).y;
                int priority = clusters.get(i).get(j).priority;
                cluster_priority+=priority;
                Graphics g = panel.getGraphics();
                g.setColor(myColors[i]);
                if(priority == 1)
                    g.fillOval(x_c, y_c, 8, 8);
                else if(priority == 2)
                    g.fillRect(x_c, y_c, 7, 7); 
                else{
                    int xpoints[] = {x_c, x_c-4, x_c+3};
                    int ypoints[] = {y_c-4, y_c+4, y_c+3};
                    int npoints = 3;
    
                    g.fillPolygon(xpoints, ypoints, npoints);
                }
                g.dispose();
            }
            System.out.println("Cluster "+(i+1)+": Size - "+clusters.get(i).size()+"\tPriority - "+cluster_priority+"\n");
        }
        button_equitable.setEnabled(false);
        button_KMeans.setEnabled(false);
        button_priority.setEnabled(true);
        button_render.setEnabled(false);
    }
    
    public static void handlePriority(ArrayList<Point> dataPoints){
        int cluster_priority = 0;
        for(int i=0;i<dataPoints.size();i++)
            cluster_priority+=dataPoints.get(i).priority;
        
        if(cluster_priority<=cluster_priority_limit){
            //System.out.println("Mark 1");
            for(int j=0;j<dataPoints.size();j++){
                int x_c = dataPoints.get(j).x;
                int y_c = dataPoints.get(j).y;
                int priority = dataPoints.get(j).priority;
                Graphics g = panel.getGraphics();
                g.setColor(myColors[color_counter]);
                if(priority == 1)
                    g.fillOval(x_c, y_c, 8, 8);
                else if(priority == 2)
                    g.fillRect(x_c, y_c, 7, 7); 
                else{
                    int xpoints[] = {x_c, x_c-4, x_c+3};
                    int ypoints[] = {y_c-4, y_c+4, y_c+3};
                    int npoints = 3;
    
                    g.fillPolygon(xpoints, ypoints, npoints);
                }
                g.dispose();
            }
            color_counter++;
            System.out.println("Cluster "+color_counter+": Size - "+dataPoints.size()+"\tPriority - "+cluster_priority+"\n");
        }
        else{
            //System.out.println("Mark 2");
            int k = (int)Math.ceil(cluster_priority/(double)cluster_priority_limit);
            ArrayList<Point> new_centroids = new ArrayList<>();
            ArrayList<Point> old_centroids = new ArrayList<>();
            ArrayList<Integer> displacement = new ArrayList<>();
            ArrayList<ArrayList<Point>> clusters = new ArrayList<>();
            
            for (int i = 0; i < k; i++) 
                clusters.add(new ArrayList<>());
            
            for (int i = 0; i < k; i++) 
                new_centroids.add(dataPoints.get(i));
            
            KMeansAlgo(k, dataPoints, old_centroids, new_centroids, displacement, clusters,false);
            decomposeCluster(k, dataPoints, new_centroids, displacement, clusters);
        }
            
    }
    
    public static void decomposeCluster(int K, ArrayList<Point> dataPoints, ArrayList<Point> new_centroids, ArrayList<Integer> displacement, ArrayList<ArrayList<Point>> clusters){
        DisplacementComparator comparator = new DisplacementComparator();
        PriorityQueue<Point> min_heap = new PriorityQueue<>(comparator);
        
        /*
        * Calculate euclidean distance of each point to each centroid
        * except its own and make two duplicate lists of these values
        * Time Complexity: O(nk)
        */
        for (Point point : dataPoints) {
            for(int i=0;i<K;i++){
                if(i != point.clusterID){
                    point.id_others.add(i);
                    point.id_others_original.add(i);
                    point.dist_others.add((int)Math.sqrt((new_centroids.get(i).x - point.x)*(new_centroids.get(i).x - point.x)+(new_centroids.get(i).y - point.y)*(new_centroids.get(i).y - point.y)));
                    point.dist_others_original.add((int)Math.sqrt((new_centroids.get(i).x - point.x)*(new_centroids.get(i).x - point.x)+(new_centroids.get(i).y - point.y)*(new_centroids.get(i).y - point.y)));
                }
            }
        }
        HashSet<Integer> hset = new HashSet<>();
        // Max Iterations: k-1
        while(true){
            //System.out.println("Mark 3");
            int max_ind = 0,max_priority=0,curr_priority=0;
            
            /*
            * Get the cluster with maximum size
            * Time Complexity: O(k)
            */
            for(int i=0;i<K;i++){
                if(!hset.contains(i)){
                    for(int j=0;j<clusters.get(i).size();j++)
                        curr_priority+=clusters.get(i).get(j).priority;
                    if(curr_priority>max_priority){
                        max_priority = clusters.get(i).size();
                        max_ind = i;
                    }
                }
                curr_priority = 0;
            }
            if(max_priority <= cluster_priority_limit)
                break;
            hset.add(max_ind);
            
            /*
            * Add each point of the cluster in Priority Queue
            * Time Complexity: O(nlogn)
            */
            for(Point point: clusters.get(max_ind)){
                min_heap.add(point);
            }
            
            
            while(!min_heap.isEmpty() && max_priority > cluster_priority_limit){
                Point point = min_heap.poll();
                //System.out.println(point.toString()+" "+point.dist_others.toString()+" "+point.id_others.toString());
                if(point.dist_others.isEmpty()){
                    point.restoreLists();
                    continue;
                }
                int min_index = point.dist_others.indexOf(Collections.min(point.dist_others));
                if(!hset.contains(point.id_others.get(min_index))){
                    clusters.get(point.clusterID).remove(point);
                    point.clusterID = point.id_others.get(min_index);
                    clusters.get(point.id_others.get(min_index)).add(point);
                    point.restoreLists();
                    max_priority-=point.priority;
                }
                else{
                    point.dist_others.remove(min_index);
                    point.id_others.remove(min_index);
                    min_heap.add(point);
                }
            }
            min_heap.clear();
        }
        
        /*
        * Recompute Final Centroids
        * Time Complexity: O(nk)
        */
        for (int i = 0; i < K; i++) {
            if (!clusters.get(i).isEmpty()) 
                new_centroids.set(i, getCentroid(clusters.get(i)));
        }
        
        /*for (int i = 0; i < new_centroids.size(); i++) {
            System.out.println("New Centroid" + (i + 1) + " " + new_centroids.get(i));
        }
        
        System.out.println("");
        
        for (int i = 0; i < clusters.size(); i++) {
            System.out.println("\nNew Cluster " + (i + 1));
            System.out.println(clusters.get(i).toString());
        }*/
        for (int i = 0; i < clusters.size(); i++) {
            //System.out.println("\nNew Cluster " + (i + 1));
            //System.out.println(clusters.get(i).toString());
            int cluster_priority = 0;
            for(int j=0;j<clusters.get(i).size();j++){
                int x_c = clusters.get(i).get(j).x;
                int y_c = clusters.get(i).get(j).y;
                int priority = clusters.get(i).get(j).priority;
                cluster_priority+=priority;
                Graphics g = panel.getGraphics();
                g.setColor(myColors[color_counter]);
                if(priority == 1)
                    g.fillOval(x_c, y_c, 8, 8);
                else if(priority == 2)
                    g.fillRect(x_c, y_c, 7, 7); 
                else{
                    int xpoints[] = {x_c, x_c-4, x_c+3};
                    int ypoints[] = {y_c-4, y_c+4, y_c+3};
                    int npoints = 3;
    
                    g.fillPolygon(xpoints, ypoints, npoints);
                }
                g.dispose();
            }
            color_counter++;
            System.out.println("Cluster "+color_counter+": Size - "+clusters.get(i).size()+"\tPriority - "+cluster_priority+"\n");
        }
        
    }
    
    public static boolean compareLists(int k, ArrayList<Point> old_centroids, ArrayList<Point> new_centroids){
        for (int i = 0; i < k; i++) {
            if(old_centroids.get(i).x != new_centroids.get(i).x || old_centroids.get(i).y != new_centroids.get(i).y)
                return true;
        }
        return false;
    }
    
    /*
    * This method detects if some cluster 
    * has more than specified no. of points
    * Time Complexity: O(k)
    */
    public static boolean detectOverflow(int cluster_size_limit){
        for(int i=0;i<clusters.size();i++){
            if(clusters.get(i).size()>cluster_size_limit)
                return true;
        }
        return false;
    }
    
    /*
    * This method computes the centroid of a cluster by
    * taking average of all points in the cluster
    * Time Complexity: O(n)
    */
    public static Point getCentroid(ArrayList<Point> list){
        int xsum = 0;
        int ysum = 0;
        int len = list.size();
        for (Point value : list) {
            xsum = xsum + value.x;
            ysum = ysum + value.y;
        }
        return new Point(xsum/len,ysum/len,0);
    }
    
}
