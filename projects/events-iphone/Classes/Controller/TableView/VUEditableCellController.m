//
//  VUEditableTableViewCellController.m
//  Events
//
//  Created by Aaron Thompson on 9/9/09.
//  Copyright 2009 Vanderbilt University. All rights reserved.
//

#import "VUEditableCellController.h"

@implementation VUEditableCellController

- (id)initWithLabel:(NSString *)aLabel
{
	self = [super init];
	if (self != nil) {
		label = [aLabel retain];
	}
	return self;
}

- (void)dealloc {
//	[cell release];
	self.delegate = nil;
    [super dealloc];
}


//
// tableView:cellForRowAtIndexPath:
//
// Returns the cell for a given indexPath.
//
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
	if (cell == nil) {
		cell = [[VUEditableCell alloc] initWithController:self];
	}
	cell.textLabel.text = self.label;
	cell.textField.text = self.value;
	[cell setEditable:isEditable];
	
	return cell;
}

//
// tableView:didSelectRowAtIndexPath:
//
// Handle row selection
//
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
	[cell setEditing:isEditable];
	
	[tableView deselectRowAtIndexPath:indexPath animated:YES];
}


- (void)setEditingField:(BOOL)isEditing
{
	isEditable = isEditing;
	[cell setEditable:isEditing];
	
	cell.textField.placeholder = (isEditable) ? @"(tap to edit)" : nil;
}


- (void)textFieldValueChanged:(NSString *)newValue
{
	self.value = newValue;
	if (delegate && [delegate respondsToSelector:@selector(cellControllerValueChanged:forKey:)]) {
		[delegate cellControllerValueChanged:self.value forKey:key];
	}
}

@synthesize label;
@synthesize value;
@synthesize key;
@synthesize delegate;

@end
