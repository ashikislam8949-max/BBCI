with open('app/src/main/java/com/example/ui/screens/DashboardScreen.kt', 'r') as f:
    content = f.read()

target = '''        // sbtech.sa Live Connected Portal Banner'''

qiwa_card_code = '''        // Qiwa Workforce Compliance & Saudization Dashboard Card
        item {
            QiwaDashboardCard(viewModel = viewModel)
        }

        // sbtech.sa Live Connected Portal Banner'''

if 'QiwaDashboardCard' not in content:
    content = content.replace(target, qiwa_card_code)

qiwa_impl = '''
@Composable
fun QiwaDashboardCard(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val qiwaResult by viewModel.qiwaResult.collectAsStateWithLifecycle()
    val isQiwaLoading by viewModel.isQiwaLoading.collectAsStateWithLifecycle()

    androidx.compose.runtime.LaunchedEffect(Unit) {
        if (qiwaResult == null) {
            viewModel.queryQiwa("7-882193")
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { viewModel.selectTab(AppNavTab.GOV_INTEGRATIONS) }
            .testTag("dashboard_qiwa_card"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE11D48).copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.People,
                            contentDescription = "Qiwa",
                            tint = Color(0xFFE11D48),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Qiwa & Mudad Workforce Compliance",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = qiwaResult?.establishmentName ?: "Hyperion Robotics KSA (Est #7-882193)",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(StatusSuccess.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = qiwaResult?.nitaqatTier ?: "Platinum (بلاتيني)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = StatusSuccess
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            val ratio = qiwaResult?.saudizationRatio ?: 62.5
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Saudization Nitaqat Ratio",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "$ratio% (Required: 40%)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SaudiGreen
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { (ratio / 100.0).toFloat().coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = StatusSuccess,
                    trackColor = StatusSuccess.copy(alpha = 0.15f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Total Workforce", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("${qiwaResult?.totalEmployees ?: 12} Employees", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text("Saudi Nationals", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("${qiwaResult?.saudiEmployees ?: 5} Local", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SaudiGreen)
                }
                Column {
                    Text("Visa Quota Balance", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("${qiwaResult?.availableVisas ?: 8} Available", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SaudiGoldDark)
                }
                Column {
                    Text("Mudad WPS", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(qiwaResult?.mudadWpsCompliance ?: "Compliant (100%)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = StatusSuccess)
                }
            }
        }
    }
}
'''

if 'fun QiwaDashboardCard' not in content:
    content = content + qiwa_impl

with open('app/src/main/java/com/example/ui/screens/DashboardScreen.kt', 'w') as f:
    f.write(content)
print('Successfully updated DashboardScreen.kt with QiwaDashboardCard')
