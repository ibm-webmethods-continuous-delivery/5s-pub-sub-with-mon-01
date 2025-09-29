# Universal Messaging Alert Configuration

## Overview

This configuration adds monitoring and alerting for Universal Messaging durable queue metrics, specifically:
- **Outstanding Events**: Number of outstanding events for durables
- **Pending Events**: Number of pending unacknowledged events for durables

## Dashboard Panel

A new panel has been added to the UM dashboard (`UM.json`) that displays both outstanding and pending events with:
- **Yellow alert threshold**: 2000 events
- **Red alert threshold**: 10000 events
- **Visual thresholds**: Color-coded background areas indicating severity levels
- **Legend**: Shows current and maximum values for each metric

## Alert Rules

Four alert rules have been created:

### Warning Alerts (Yellow - 2000 threshold)
- **UM Outstanding Events Warning**: Triggers when outstanding events ≥ 2000 (fires after 2 minutes)
- **UM Pending Events Warning**: Triggers when pending events ≥ 2000 (fires after 2 minutes)

### Critical Alerts (Red - 10000 threshold)
- **UM Outstanding Events Critical**: Triggers when outstanding events ≥ 10000 (fires after 1 minute)
- **UM Pending Events Critical**: Triggers when pending events ≥ 10000 (fires after 1 minute)

## Email Configuration

### Required Setup Steps

1. **Update Email Address**: Edit `contact-points.yaml` and replace `your-email@company.com` with actual email addresses
2. **Configure SMTP**: Add SMTP configuration to your Grafana environment variables or configuration file

### Docker Compose Environment Variables

Add these environment variables to the `grafana` service in your `docker-compose.yml`:

```yaml
grafana:
  image: grafana/grafana-oss
  environment:
    # SMTP Configuration
    - GF_SMTP_ENABLED=true
    - GF_SMTP_HOST=your-smtp-server:587
    - GF_SMTP_USER=your-smtp-username
    - GF_SMTP_PASSWORD=your-smtp-password
    - GF_SMTP_FROM_ADDRESS=grafana@your-domain.com
    - GF_SMTP_FROM_NAME=Grafana UM Alerts
    # Security settings
    - GF_SMTP_SKIP_VERIFY=false
    # Enable alerting
    - GF_ALERTING_ENABLED=true
    - GF_UNIFIED_ALERTING_ENABLED=true
```

### Alternative SMTP Configuration

You can also configure SMTP in a Grafana configuration file:

```ini
[smtp]
enabled = true
host = your-smtp-server:587
user = your-smtp-username
password = your-smtp-password
from_address = grafana@your-domain.com
from_name = Grafana UM Alerts
skip_verify = false
```

## File Structure

```
config/grafana/provisioning/
├── alerting/
│   ├── alert-rules.yaml          # Alert rule definitions
│   ├── contact-points.yaml       # Email notification configuration
│   └── notification-policies.yaml # Alert routing policies
├── dashboards/
│   └── MyTests/UniversalMessaging/
│       └── UM.json              # Updated dashboard with new panel
└── datasources/
    └── myDataSources.yaml       # Prometheus datasource configuration
```

## Metrics Being Monitored

Based on the example metrics file, the alerts monitor:

```
sag_um_topic_durable_outstanding{durable_name="Demo-5s-pub-sub-test-01##Example__Subscriber__receiveAMessage",durable_type="Shared",topic_name="destinationName=/wm/is/Canonicals/Example/demoMessage"}

sag_um_topic_durable_pending{durable_name="Demo-5s-pub-sub-test-01##Example__Subscriber__receiveAMessage",durable_type="Shared",topic_name="destinationName=/wm/is/Canonicals/Example/demoMessage"}
```

## Testing the Configuration

1. **Start the stack**: `docker-compose up -d`
2. **Access Grafana**: `http://localhost:${PSM_TEST_HOST_PORT_PREFIX}30` (typically port 3030)
3. **Check the dashboard**: Navigate to the UM dashboard and verify the new panel appears
4. **Test alerts**: You can simulate high values by temporarily modifying alert thresholds for testing
5. **Verify email delivery**: Ensure SMTP configuration is working by triggering a test alert

## Troubleshooting

- **Alerts not firing**: Check Grafana logs for evaluation errors
- **Emails not sending**: Verify SMTP configuration and check Grafana logs
- **Dashboard not loading**: Ensure datasource UID matches in all configurations
- **Missing metrics**: Verify Universal Messaging is exposing metrics on the expected endpoint

## Customization

- **Thresholds**: Modify the threshold values in `alert-rules.yaml`
- **Email template**: Customize the message format in `contact-points.yaml`
- **Alert frequency**: Adjust `group_interval` and `repeat_interval` in `notification-policies.yaml`
- **Additional recipients**: Add more email addresses or create additional contact points
