package org.github.arosecra.brooke;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.thymeleaf.util.StringUtils;

public class Main11 {
	public static void main(String[] args) throws Exception {
		File tempDir = new File("D:\\video\\temp");
		
		List<String> groups = new ArrayList<>();
		
		for(File file : tempDir.listFiles()) {
			if(file.getName().endsWith("_Chapters.xml")) {
				groups.add(StringUtils.substringBefore(file.getName(), "_Chapters.xml"));
			}
		}
		
		boolean assumeSeries = true;
		String[] series = new String[] {
				"Akashic_Records",
				"Angel_Beats",
				"Appleseed_XIII",
				"Astra_Lost_in_Space",
				"Azumanga_Daioh",
				"Baccano",
				"Ben_To",
				"Berserk",
				"Beyond_the_Boundary",
				"Black_Cat",
				"Black_Lagoon",
				"Boogie_Pop_Phantom",
				"Book_of_Bantora",
				"Bunny_Drop",
				"Chaos_Head",
				"Chrome_Shelled_Regios",
				"Clockwork_Planet",
				"Code_Geass",
				"Code_Geass_Akito",
				"Coppelion",
				"Cowboy_Bebop",
				"C_Control",
				"Dan_Machi",
				"Darker_than_Black",
				"Darling_In_The_Franxx",
				"Demon_King_Daimao",
				"Desert_Punk",
				"Devil_is_a_Part_Timer",
				"Dot_Hack_SIGN",
				"Eden_of_the_East_01",
				"Eden_of_the_East_02_King_of_Eden",
				"Eden_of_the_East_03_Paradise_Lost",
				"EF_-_A_Tale_of_Melodies",
				"Elfen_Lied",
				"Ergo_Proxy",
				"Excel_Saga",
				"Fairy_Tail",
				"Fairy_Tail_Zero",
				"Flag",
				"Fractal",
				"Full_Metal_Panic_01",
				"Full_Metal_Panic_02_Fumoffu",
				"Full_Metal_Panic_03_Second_Raid",
				"Full_Metal_Panic_04_Invisible_Victory",
				"Fuuka",
				"Gankutsuou_Count_of_Monte_Cristo",
				"Gargantia",
				"Get_Backers",
				"Ghost_in_the_Shell_Arise",
				"Ghost_in_the_Shell_SAC_01",
				"Ghost_in_the_Shell_SAC_02",
				"Gintama",
				"Glass_Fleet",
				"Grimgar",
				"Gundam_OO",
				"Gundam_SEED",
				"Gundam_SEED_02_Destiny",
				"Gundam_Wing",
				"Haganai",
				"Haibane_Renmei",
				"Haikyuu",
				"Hellsing",
				"High_School_of_the_Dead",
				"Hinamatsuri",
				"Humanity_has_Declined",
				"Interview_with_Monster_Girls",
				"Jormungand",
				"Kill_Me_Baby",
				"Kinos_Journey",
				"Konosuba",
				"Kore_Wa_Zombie_Desu_Ka",
				"Kurau",
				"K_On",
				"Last_Exile",
				"Legend_of_the_Legendary_Heroes",
				"Log_Horizon",
				"Love_Hina",
				"Maid_Sama",
				"March_Comes_in_Like_a_Lion",
				"Mayou",
				"Miss_Kobayashi",
				"Mitsuboshi_Colors",
				"Monogatari",
				"Moribito",
				"Negima",
				"Nichijou",
				"Nisekoi",
				"Noir",
				"Ore_Monogatari",
				"Outlaw_Star",
				"Phantom",
				"Psycho_Pass",
				"Read_Or_Die",
				"Robotics;Notes",
				"Sacred_Blacksmith",
				"Saga_Of_Tanya_The_Evil",
				"Samurai_7",
				"Samurai_Champloo",
				"Sands_of_Destruction",
				"Sekai",
				"Serial_Experiments_Lain",
				"Shangri_La",
				"SKET_Dance",
				"Slayers",
				"Sound_of_the_Sky",
				"Spice_and_Wolf",
				"Steins;Gate",
				"Strike_Witches",
				"Sword_Art_Online",
				"Tenchi_Muyou_War_on_Geminar",
				"Texhnolyze",
				"That_Time_I_Was_Reincarnated_As_A_Slime",
				"Those_Who_Hunt_Elves",
				"Toaru_Raildex",
				"Tower_of_Druaga",
				"To_Loveru",
				"Trigun",
				"Tsubasa",
				"Twelve_Kingdoms",
				"Utaware",
				"Witch_Hunter_Robin",
				"Wolf's_Rain",
				"World_End",
				"X",
				"XXXHolic",
				"Your_Lie_in_April"
				
		};
		
		
		
		
		for(String group : groups) {
			File groupDir = new File(tempDir, group);
			groupDir.mkdirs();

			for(File file : tempDir.listFiles()) {
				if(file.isFile() && file.getName().startsWith(group)) {
					FileUtils.moveFileToDirectory(file, groupDir, false);
				}
			}
		}
		
		//sort them into series buckets
		for(String ser : series) {
			for(File file : tempDir.listFiles()) {
				if(file.isDirectory() && 
						file.getName().startsWith(ser) && 
						!file.getName().equals(ser)) {
					File seriesDir = new File(tempDir, ser);
					seriesDir.mkdirs();
					
					FileUtils.moveDirectoryToDirectory(file, seriesDir, true);
				}
			}
		}
		
	}
}
