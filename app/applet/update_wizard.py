with open('app/src/main/java/com/example/ui/screens/MisaCrWizardScreen.kt', 'r') as f:
    content = f.read()

if 'import com.example.ui.AppNavTab' not in content:
    content = content.replace('import com.example.ui.MainViewModel', 'import com.example.ui.AppNavTab\nimport com.example.ui.MainViewModel')

target = '''            SaudiBrandHeader(
                title = "MISA CR & Government Integration",
                subtitle = "7-Step seamless company registration and digital regulatory issuance"
            )'''

banner_code = '''            SaudiBrandHeader(
                title = "MISA CR & Government Integration",
                subtitle = "7-Step seamless company registration and digital regulatory issuance"
            )
        }
        // Launch Multi-Step MISA Application Form Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.selectTab(AppNavTab.MISA_APPLICATION) }
                    .testTag("card_launch_misa_form"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3A2F)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "New: Multi-Step MISA e-License Form",
                            color = Color(0xFF86EFAC),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Launch interactive 5-step form for company registration, business activity selection, and capital filing.",
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFD4AF37))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Start Form →",
                            color = Color(0xFF1E241F),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }'''

if 'card_launch_misa_form' not in content:
    content = content.replace(target, banner_code)

with open('app/src/main/java/com/example/ui/screens/MisaCrWizardScreen.kt', 'w') as f:
    f.write(content)
print('Successfully updated MisaCrWizardScreen.kt')
