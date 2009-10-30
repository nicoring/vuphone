// 
//  Event.m
//  Events
//
//  Created by Aaron Thompson on 9/9/09.
//  Copyright 2009 Iostudio, LLC. All rights reserved.
//

#import "Event.h"
#import "Location.h"
#import "EntityConstants.h"

static NSDateFormatter *dateFormatter;

@implementation Event 

@dynamic ownerAndroidId;
@dynamic source;
@dynamic url;
@dynamic name;
@dynamic startTime;
@dynamic details;
@dynamic endTime;
@dynamic location;
@dynamic serverId;

+ (NSArray *)allSources {
	return [NSArray arrayWithObjects:
			@"Official Calendar",
			@"Commons",
			@"Athletics",
			@"Facebook",
			VUEventSourceUser,
			nil];
}

- (NSString *)startDateString {
	if (!dateFormatter) {
		dateFormatter = [[NSDateFormatter alloc] init];
		[dateFormatter setDateFormat:@"eeee, MMMM d"];
	}
	
	if (self.startTime) {
		return [dateFormatter stringFromDate:self.startTime];
	} else {
		// There is some sort of error, so just return today's date
		return [dateFormatter stringFromDate:[NSDate date]];
	}
}

- (BOOL)isEditableByDeviceWithId:(NSString *)deviceId {
	return [self.ownerAndroidId isEqualToString:deviceId];
}

#pragma mark MKAnnotation methods

- (NSString *)title {
	return self.name;
}

- (NSString *)subtitle {
	return self.location.name;
}

- (CLLocationCoordinate2D)coordinate {
	return [self.location coordinate];
}
	
- (void)dealloc
{
	if (dateFormatter) {
		[dateFormatter release];
	}
	[super dealloc];
}

@end
