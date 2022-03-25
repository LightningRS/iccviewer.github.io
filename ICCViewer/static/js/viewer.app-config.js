/**
 * viewer.app-config.js
 */

 window.ICCTagViewer = window.ICCTagViewer ? window.ICCTagViewer : {};
 $.extend(window.ICCTagViewer, {
     preDefinedApps: [
         {
             "name": "============= Benchmarks ============="
         },
         {
             "name": "DroidBench",
             "pkgName": "bm_DroidBench",
             "apkFile": "bm_DroidBench_apks.zip",
             "srcPackFile": "https://drive.google.com/uc?id=1xYDuhcJtkTqqZAorzbNV9RTwP3lwVMpM&export=download&confirm=t"
         },
         {
             "name": "ICCBench",
             "pkgName": "bm_ICCBench",
             "apkFile": "https://drive.google.com/uc?id=19SnwJONQbxZEbjBKEuTI80TS5kL5NvKP&export=download&confirm=t",
             "srcPackFile": "https://drive.google.com/uc?id=1FUjaabaodzzCyz3vRWcpHt1t9PsdJmZ9&export=download&confirm=t"
         },
         {
             "name": "ICCBotBench",
             "pkgName": "bm_ICCBotBench",
             "apkFile": "https://drive.google.com/uc?id=1uE_G1FsJN1WrkM3ZMYMjAXLNMg6IoTNY&export=download&confirm=t",
             "srcPackFile": "https://drive.google.com/uc?id=1uvNteHYSTiOKD3t5MEMWiW-TYaNc6QE0&export=download&confirm=t"
         },
         {
             "name": "RAICC",
             "pkgName": "bm_RAICC",
             "apkFile": "https://drive.google.com/uc?id=1abBV5fSyp03rqMzvD8eNY53AYRDc7FMu&export=download&confirm=t",
             "srcPackFile": "https://drive.google.com/uc?id=1ISCTe41FdXxtv_2f3bgaoVyqz2V4tDUY&export=download&confirm=t"
         },
         {
             "name": "storyBoard",
             "pkgName": "bm_storyBoard",
             "apkFile": "https://drive.google.com/uc?id=18NuRf-VI_rFTMe7UlM9YdkyDX15bUBh-&export=download&confirm=t",
             "srcPackFile": "https://drive.google.com/uc?id=1_FC5mNeNWaSj9jSGG0o1r6lkoh7X0UMn&export=download&confirm=t"
         },
 
         {
             "name": "========== Open-source Apps =========="
         },
         {
             "name": "1Sheeld 1.9.0 (190401)",
             "pkgName": "com.integreight.onesheeld",
             "apkFile": "https://drive.google.com/uc?id=1_miIiS4pWO5L2-cbQdh-Mt6QVhfNvvz-&export=download&confirm=t",
             "srcPackFile": "https://drive.google.com/uc?id=1wJBih8CKtZZt_VDWsRTlhigcTfdjGFgR&export=download&confirm=t"
         },
         {
             "name": "AFWall+ 3.5.2.1 (20210517)",
             "pkgName": "dev.ukanth.ufirewall",
             "apkFile": "https://drive.google.com/uc?id=1m9ZhNH0VxGVE9eAdDBZnFSQVl8uU4HqG&export=download&confirm=t",
             "srcPackFile": "https://drive.google.com/uc?id=1Q2LXWLEXDfeHpKfCWbpr_HWQUppS6O7g&export=download&confirm=t"
         },
         {
             "name": "AnkiDroid 2.14.6 (21406300)",
             "pkgName": "com.ichi2.anki",
             "apkFile": "https://drive.google.com/uc?id=1Y640XFWyL4i_0SmJEMr0t-FUS4_SV3Kq&export=download&confirm=t",
             "srcPackFile": "https://drive.google.com/uc?id=1lVjhIo85VU_25KEPAqwRXxJbtfIUUztZ&export=download&confirm=t"
         },
         {
             "name": "AntennaPod 2.2.1 (2020195)",
             "pkgName": "de.danoeh.antennapod",
             "apkFile": "https://drive.google.com/uc?id=1C24t-KfLbKmGH94Yfg3VO7y8U50_jhCH&export=download&confirm=t",
             "srcPackFile": "https://drive.google.com/uc?id=1dRCIU-wxOg4E5CGTuIqtP3UseH649DA5&export=download&confirm=t"
         },
         {
             "name": "AnyMemo 10.9.993 (223)",
             "pkgName": "org.liberty.android.fantastischmemo",
             "apkFile": "https://drive.google.com/uc?id=19z-bgDWVvpdIPR_ybBul0GvgSfl4upAX&export=download&confirm=t",
             "srcPackFile": "https://drive.google.com/uc?id=1xu2qAaxRCg2LjGz-gx2gAU-oJo1nhHsr&export=download&confirm=t"
         },
         {
             "name": "Calendula 2.5.11 (42)",
             "pkgName": "es.usc.citius.servando.calendula",
             "apkFile": "https://drive.google.com/uc?id=1GzIYXL3xb6fSDTk-45m4syQAxnrHrbCC&export=download&confirm=t",
             "srcPackFile": "https://drive.google.com/uc?id=1XnaJI4nnBzdYpTfiHpH33jvRXt2R81RG&export=download&confirm=t"
         },
         {
             "name": "Conversations 2.9.13+fcr (42015)",
             "pkgName": "eu.siacs.conversations",
             "apkFile": "https://drive.google.com/uc?id=18a4k6rzXwOhPK2c63Pq7iggWbhCLCnv8&export=download&confirm=t",
             "srcPackFile": "https://drive.google.com/uc?id=1MQeIT85kUNHTOHzQv9kA0ntrIsfyvjSj&export=download&confirm=t"
         },
         {
             "name": "CSipSimple 0.04-01 (1841)",
             "pkgName": "com.csipsimple",
             "apkFile": "https://drive.google.com/uc?id=1HK3KwgkO_rvjNhrBNImgHDb89isquBI5&export=download&confirm=t",
             "srcPackFile": "https://drive.google.com/uc?id=10_FZ3dYslkFLGEMR0e6HBtwDHZz8MKxG&export=download&confirm=t"
         },
         {
             "name": "Easy Diary 1.4.167 (233)",
             "pkgName": "me.blog.korn123.easydiary",
             "apkFile": "https://drive.google.com/uc?id=1axjIJhhZ9IT1oqzzAWQSiZWZfA0ufuTI&export=download&confirm=t",
             "srcPackFile": "https://drive.google.com/uc?id=122f8kYq1n1BhYCagrtRNgBCwmqXAvRTI&export=download&confirm=t"
         },
         {
             "name": "EP Mobile 2.25.4 (69)",
             "pkgName": "org.epstudios.epmobile",
             "apkFile": "https://drive.google.com/uc?id=1k4AJBLT9zeXivicp3s9aLLAfF379LYhj&export=download&confirm=t",
             "srcPackFile": "https://drive.google.com/uc?id=129JHlxNTrCivc19Z5ZpjRD4dQziaQKP9&export=download&confirm=t"
         },
         {
             "name": "EteSync 2.2.4 (20204)",
             "pkgName": "com.etesync.syncadapter",
             "apkFile": "https://drive.google.com/uc?id=1pGp_qu16JDRX1ICdlz_ohNBTFOS7vVvC&export=download&confirm=t",
             "srcPackFile": "https://drive.google.com/uc?id=1Ikn7SBCAUWAeUp4QweSY2TFfuBFW4zj1&export=download&confirm=t"
         },
         {
             "name": "iNaturalist 1.22.1 (488)",
             "pkgName": "org.inaturalist.android",
             "apkFile": "https://drive.google.com/uc?id=10Nakmejw1QY1qc6QJ0IgockWAA7N74Ul&export=download&confirm=t",
             "srcPackFile": "https://drive.google.com/uc?id=1UcCYjCfnoavqZ5Nv5yPGVHjwd9cY96Cw&export=download&confirm=t"
         },
         {
             "name": "K-9 Mail 5.734 (27034)",
             "pkgName": "com.fsck.k9",
             "apkFile": "https://drive.google.com/uc?id=1D9UFxR_TpCmVP_9258a2x_IdaTKiE75X&export=download&confirm=t",
             "srcPackFile": "https://drive.google.com/uc?id=1uIB2l3is5fBfrKdOw2yYjhLW3Xhjdw8n&export=download&confirm=t"
         },
         {
             "name": "LinCal 1.3.1 (13)",
             "pkgName": "felixwiemuth.lincal",
             "apkFile": "https://drive.google.com/uc?id=1CRvhQ2aV0vdLx-_QDO8CePVJ8ds4Ll30&export=download&confirm=t",
             "srcPackFile": "https://drive.google.com/uc?id=1FT8s_lt2LOftCDUVqjBoNbNp4cjpEMVE&export=download&confirm=t"
         },
         {
             "name": "MoneyWallet 4.0.2-floss (55)",
             "pkgName": "com.oriondev.moneywallet",
             "apkFile": "https://drive.google.com/uc?id=1ARxhFEJdP-U8vye8653hG9imCOF5bBMp&export=download&confirm=t",
             "srcPackFile": "https://drive.google.com/uc?id=1nzcvttbmSyhGN1UMZOqadeKDu20czidO&export=download&confirm=t"
         },
         {
             "name": "Open GPS Tracker 1.3.5 (85)",
             "pkgName": "nl.sogeti.android.gpstracker",
             "apkFile": "https://drive.google.com/uc?id=1BrAvtxpNApRcHxXPGAbYpivcXUbVK6UA&export=download&confirm=t",
             "srcPackFile": "https://drive.google.com/uc?id=1h3EaKmnhqApe7-PfvE-7otiJjE-1SERW&export=download&confirm=t"
         },
         {
             "name": "OpenKeychain 5.7.5 (57500)",
             "pkgName": "org.sufficientlysecure.keychain",
             "apkFile": "https://drive.google.com/uc?id=1jPnS9s3Y8Bfk5E42NgAxmDnyRJ1GcQi1&export=download&confirm=t",
             "srcPackFile": "https://drive.google.com/uc?id=1Rfa1edh6FAZKNLiJvNqnhgXJRdWH2jlc&export=download&confirm=t"
         },
         {
             "name": "OsmAnd~ 3.9.10 (400)",
             "pkgName": "net.osmand.plus",
             "apkFile": "https://drive.google.com/uc?id=1X3cS0Xj7IP68P42qjFJcDGEJoVHvezsX&export=download&confirm=t",
             "srcPackFile": "https://drive.google.com/uc?id=1v3p9ApN5u6P7_H1rduRillyucXn2iXW1&export=download&confirm=t"
         },
         {
             "name": "Padland 1.8 (23)",
             "pkgName": "com.mikifus.padland",
             "apkFile": "https://drive.google.com/uc?id=1JKEwXbEoQm4n9WGSYf63PtIaFkrMbzUB&export=download&confirm=t",
             "srcPackFile": "https://drive.google.com/uc?id=1YnFL3lbFM050acQt9le8271gil0ZYW-d&export=download&confirm=t"
         },
         {
             "name": "PassAndroid 3.5.7 (357)",
             "pkgName": "org.ligi.passandroid",
             "apkFile": "https://drive.google.com/uc?id=1vUjkzBgdbE37Je7BhgdvuRvqA7RkIINH&export=download&confirm=t",
             "srcPackFile": "https://drive.google.com/uc?id=1HskDcgd9BE_QwZA92GNWceyaw9XguwrH&export=download&confirm=t"
         },
         {
             "name": "Silence 0.16.12-unstable (211)",
             "pkgName": "org.smssecure.smssecure",
             "apkFile": "https://drive.google.com/uc?id=1qV-B5DdMZb-ltAAhGm945P6Ijq-wy8Xp&export=download&confirm=t",
             "srcPackFile": "https://drive.google.com/uc?id=1Y-mj-B02-KSFFZ8qbszGmjgAc3BLJ0dJ&export=download&confirm=t"
         },
         {
             "name": "Simple Solitaire Collection 3.13 (71)",
             "pkgName": "de.tobiasbielefeld.solitaire",
             "apkFile": "https://drive.google.com/uc?id=1y_cPor_Hd9Jc9wT7w3dUDdY5Ol9n1WFd&export=download&confirm=t",
             "srcPackFile": "https://drive.google.com/uc?id=1SybeFDs6z4jPZxpSLy_0T8QtrJDVk6es&export=download&confirm=t"
         },
         {
             "name": "SteamGifts 1.5.13 (1005513)",
             "pkgName": "net.mabako.steamgifts",
             "apkFile": "https://drive.google.com/uc?id=1ADUBeovsViEovV9dIE5LOkVmLtp0KgAD&export=download&confirm=t",
             "srcPackFile": "https://drive.google.com/uc?id=1PlAKEK_brm0hbaaE65UDiKJEJPyyW13T&export=download&confirm=t"
         },
         {
             "name": "Suntimes 0.13.10 (74)",
             "pkgName": "com.forrestguice.suntimeswidget",
             "apkFile": "https://drive.google.com/uc?id=1D9UFxR_TpCmVP_9258a2x_IdaTKiE75X&export=download&confirm=t",
             "srcPackFile": "https://drive.google.com/uc?id=19Me0J7ipEEltAMn-NoinbMRT6f5lDyCT&export=download&confirm=t"
         },
         {
             "name": "Syncthing 1.16.0 (4273)",
             "pkgName": "com.nutomic.syncthingandroid",
             "apkFile": "https://drive.google.com/uc?id=1hh8rUYgW6im-JqWTKsBobgnbTTy2Jxb3&export=download&confirm=t",
             "srcPackFile": "https://drive.google.com/uc?id=14d_-JCR6eL4qsyq1B3LQzM4-q8b6IAl3&export=download&confirm=t"
         },
         {
             "name": "Taskbar 6.1.1 (203)",
             "pkgName": "com.farmerbb.taskbar",
             "apkFile": "https://drive.google.com/uc?id=1lJNBZki3nunYS2TQaLD-YKKb9hVhB3tP&export=download&confirm=t",
             "srcPackFile": "https://drive.google.com/uc?id=11_MSJxfd8gaN_IvVJuKseS1A0erzBxUa&export=download&confirm=t"
         },
         {
             "name": "Tasks 11.10 (111004)",
             "pkgName": "org.tasks",
             "apkFile": "https://drive.google.com/uc?id=1yEC480uPLJDZ6WhYOZuO6iEL-J0lIxDE&export=download&confirm=t",
             "srcPackFile": "https://drive.google.com/uc?id=1ApGhLaruX1UFZA-c21heQUz8jHX_Qkz3&export=download&confirm=t"
         },
         {
             "name": "Titan Companion v67-beta (67)",
             "pkgName": "pt.joaomneto.titancompanion",
             "apkFile": "https://drive.google.com/uc?id=1v8q0StjGamT7exV3hSx3cblc05YRghj8&export=download&confirm=t",
             "srcPackFile": "https://drive.google.com/uc?id=1MdJgZYyL4XrKnQFuDxK9SxN6kcHkkc4-&export=download&confirm=t"
         },
         {
             "name": "TopoSuite 1.2.0 (69)",
             "pkgName": "ch.hgdev.toposuite",
             "apkFile": "https://drive.google.com/uc?id=1mY1M_CgjNA_spOc64tgaa3ErsoVkQbVi&export=download&confirm=t",
             "srcPackFile": "https://drive.google.com/uc?id=1sto0HIJn0-0_r_2PpVfeZrjo1x_FastW&export=download&confirm=t"
         },
         {
             "name": "TopoSuite 1.2.0 (69) [without public tab]",
             "pkgName": "ch.hgdev.toposuite",
             "oracleFile": "ch.hgdev.toposuite_oracle_without_public_tab.xml",
             "apkFile": "https://drive.google.com/uc?id=1mY1M_CgjNA_spOc64tgaa3ErsoVkQbVi&export=download&confirm=t",
             "srcPackFile": "https://drive.google.com/uc?id=1sto0HIJn0-0_r_2PpVfeZrjo1x_FastW&export=download&confirm=t"
         },
         {
             "name": "Transports Rennes 4.1.3 (413)",
             "pkgName": "fr.ybo.transportsrennes",
             "apkFile": "https://drive.google.com/uc?id=1MNF2L5u3CGFaFHmVr5MWZ2fVtPrsfBDK&export=download&confirm=t",
             "srcPackFile": "https://drive.google.com/uc?id=1gB52YQjJdbEaJJN4d5ue8AHjxEt8bSvW&export=download&confirm=t"
         },
         {
             "name": "WorkTime 1.1.15 (270)",
             "pkgName": "eu.vranckaert.worktime",
             "apkFile": "https://drive.google.com/uc?id=1FCK34IPpIu7bMIwyxfKVYwpj9CTMwfHu&export=download&confirm=t",
             "srcPackFile": "https://drive.google.com/uc?id=1ZDX2XwkzbfWxUSNigDnCIGmgSq16Kl7W&export=download&confirm=t"
         }
     ]
 });