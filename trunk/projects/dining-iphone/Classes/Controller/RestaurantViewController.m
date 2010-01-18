//
//  RestaurantViewController.m
//  Dining
//
//  Created by Aaron Thompson on 1/13/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "RestaurantViewController.h"
#import "HourRange.h"
#import "DiningAppDelegate.h"
#import "ImageViewCell.h"
#import "VariableHeightCell.h"

@implementation RestaurantViewController

@synthesize restaurant;


- (void)viewWillAppear:(BOOL)animated
{
	[super viewWillAppear:animated];
	
	self.navigationItem.title = restaurant.name;
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
	if (restaurant.details != nil && [restaurant.details length] > 0) {
		return 5;
	} else {
		return 4;
	}
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section 
{
	switch (section) {
		case 0:
			// Image
			return nil;
		case 1:
			return [@"Type: " stringByAppendingString:restaurant.type];
		case 2:
			return [@"Distance: " stringByAppendingString:[restaurant distanceAsString]];
		case 3:
			return @"Hours";
		case 4:
			return @"Details";
		case 5:
			// Web site
			return nil;
		default:
			return nil;
	}
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
	switch (section) {
		case 1:
			return 0;
		case 3:
			return [restaurant.openHours count];
		default:
			return 1;
	}
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
	static NSString *imageIdentifier = @"imageID";
	static NSString *varHeightIdentifier = @"varHeightID";
	static NSString *defaultIdentifier = @"defaultID";
	
	UITableViewCell *cell;
	VariableHeightCell *varCell;
	ImageViewCell *imageCell;
	
	// Determine which cell to reuse.
	switch (indexPath.section) {
		case 0:
			cell = [tableView dequeueReusableCellWithIdentifier:imageIdentifier];
			break;
		case 4:
			cell = [tableView dequeueReusableCellWithIdentifier:varHeightIdentifier];
			break;
		default:
			cell = [tableView dequeueReusableCellWithIdentifier:defaultIdentifier];
			break;
	}

	// Determine which cell type to create if needed.
	if (cell == nil)
	{
		switch (indexPath.section) {
			case 0:
				cell = [[[ImageViewCell alloc] initWithStyle:UITableViewCellStyleDefault
												reuseIdentifier:imageIdentifier] autorelease];
				break;
			case 4:
				cell = [[[VariableHeightCell alloc] initWithStyle:UITableViewCellStyleDefault 
											  reuseIdentifier:varHeightIdentifier] autorelease];
				break;
			default:
				cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleValue2
											   reuseIdentifier:defaultIdentifier] autorelease];

		}
	}

	imageCell = (ImageViewCell *)cell;
	varCell = (VariableHeightCell *)cell;
	
	HourRange *range;
	
	// Configure the cell.
	switch (indexPath.section)
	{
		case 0:	// Image
			[imageCell setImage:[restaurant image]];
			break;
			
		case 1: // Type
			cell.textLabel.text = restaurant.type;
			break;
			
		case 2: // Distance
			cell.detailTextLabel.text = @"Show on Map";
			break;
			
		case 3: // Hours
			range = (HourRange *)[[restaurant.openHours allObjects] objectAtIndex:indexPath.row];
			cell.textLabel.text = [range.day capitalizedString];
			cell.detailTextLabel.text = [range formattedHoursString];
			break;

		case 4:
			[varCell setText:restaurant.details];
			break;
		default:
			break;
	}
	
	return cell;
}


- (CGFloat)tableView:(UITableView *)aTableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
	UITableViewCell *cell = [self tableView:aTableView cellForRowAtIndexPath:indexPath];
	switch (indexPath.section) {
		case 0:
			return [(ImageViewCell *)cell height];
		case 3:
			return 30.0f;
		case 4:
			return [(VariableHeightCell *)cell height];
		default:
			return 44.0;
	}
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
	if (indexPath.section == 2) {
		DiningAppDelegate *delegate = [[UIApplication sharedApplication] delegate];
		[delegate showRestaurantOnMap:restaurant];
	}
	
	[tableView deselectRowAtIndexPath:indexPath animated:NO];
}

- (void)didReceiveMemoryWarning {
	// Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
	
	// Release any cached data, images, etc that aren't in use.
}


- (void)dealloc {
	self.restaurant = nil;

    [super dealloc];
}


@end
