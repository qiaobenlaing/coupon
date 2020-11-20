//
//  StringInputTableViewCell.m
//  ShootStudio
//
//  Created by Tom Fewster on 19/10/2011.
//  Copyright (c) 2011 __MyCompanyName__. All rights reserved.
//

#import "StringInputTableViewCell.h"
#import "sys/utsname.h"


#define IPHONE_KEYBOARD_HIEGHT 216+44

@implementation StringInputTableViewCell

@synthesize delegate;
@synthesize stringValue;
@synthesize textField;
@synthesize key;



- (void)initalizeInputView {
	// Initialization code
	self.selectionStyle = UITableViewCellSelectionStyleNone;
	self.textField = [[UITextField alloc] initWithFrame:CGRectMake(120, 0, APP_VIEW_WIDTH-140, 44)];
	self.textField.autocorrectionType = UITextAutocorrectionTypeDefault;
	self.textField.autocapitalizationType = UITextAutocapitalizationTypeWords;
	self.textField.textColor = [UIColor blueColor];
	self.textField.font = [UIFont systemFontOfSize:17.0f];
	self.textField.clearButtonMode = UITextFieldViewModeNever;
	self.textField.autoresizingMask = UIViewAutoresizingFlexibleWidth;
    self.textField.backgroundColor = [UIColor clearColor];
	[self addSubview:self.textField];
	
	self.accessoryType = UITableViewCellAccessoryNone;
	
	self.textField.delegate = self;
}

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
		[self initalizeInputView];
    }
    return self;
}

- (id)initWithCoder:(NSCoder *)aDecoder {
    self = [super initWithCoder:aDecoder];
    if (self) {
		[self initalizeInputView];
    }
    return self;
}

- (void)setSelected:(BOOL)selected {
	[super setSelected:selected];
	if (selected) {
		[self.textField becomeFirstResponder];
	}
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
	[super setSelected:selected animated:animated];
	if (selected) {
		[self.textField becomeFirstResponder];
	}
}

- (void)setStringValue:(NSString *)value {
	self.textField.text = value;
}

- (NSString *)stringValue {
	return self.textField.text;
}

-(void)textFieldDidBeginEditing:(UITextField *)textField_{
    
    [self.textField becomeFirstResponder];
    
    
    if ([[[UIDevice currentDevice] systemVersion] floatValue] >= 5.0) {
        // iOS 5 code
    }
    else {
        // iOS 4.x code
        
        
//        UITableView *tableView = (UITableView *)self.superview;
//        
//        if (tableView.frame.size.height- textField_.superview.frame.origin.y<IPHONE_KEYBOARD_HIEGHT) {
//            
//            
//            
//            UITableView *tableView = (UITableView *)self.superview;
//            
//            [tableView setContentOffset:CGPointMake(0.0, textField.superview.frame.origin.y+44-IPHONE_KEYBOARD_HIEGHT) animated:YES];
//        }
        
        
        
    }
    
    
    
    //[tableView setContentOffset:CGPointMake(0.0, 300) animated:YES];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
	[self.textField resignFirstResponder];
    
    if ([[[UIDevice currentDevice] systemVersion] floatValue] >= 5.0) {
        // iOS 5 code
    }
    else {
        // iOS 4.x code
//        
//        UITableView *tableView = (UITableView *)self.superview;
//        [tableView setContentOffset:CGPointMake(0.0, 0.0) animated:YES];
        
        
    }
    
    return YES;
}



- (void)textFieldDidEndEditing:(UITextField *)textField {
	if (delegate && [delegate respondsToSelector:@selector(tableViewCell:didEndEditingWithString:)]) {
		[delegate tableViewCell:self didEndEditingWithString:self.stringValue];
	}
//	UITableView *tableView = (UITableView *)self.superview;
//	[tableView deselectRowAtIndexPath:[tableView indexPathForCell:self] animated:YES];
    
    if ([[[UIDevice currentDevice] systemVersion] floatValue] >= 5.0) {
        // iOS 5 code
    }
    else {
        // iOS 4.x code
        
        
//        [tableView setContentOffset:CGPointMake(0.0, 0.0) animated:YES];
        
        
    }
    
}

//- (void)layoutSubviews {
//	[super layoutSubviews];
//    
//    self.separatorInset = UIEdgeInsetsZero;
//    
//	CGRect editFrame = CGRectInset(self.contentView.frame, 10, 10);
//	
//	if (self.textLabel.text && [self.textLabel.text length] != 0) {
//		CGSize textSize = [self.textLabel sizeThatFits:CGSizeZero];
//		editFrame.origin.x += textSize.width + 10;
//		editFrame.size.width -= textSize.width + 10;
//        
//        if (self.detailTextLabel && [self.detailTextLabel.text length] != 0) {
//            CGSize detailTextSize = [self.detailTextLabel sizeThatFits:CGSizeZero];
//            editFrame.size.width -= detailTextSize.width +20 ;
//        }
//        
//		self.textField.textAlignment = NSTextAlignmentRight;
//	} else {
//		self.textField.textAlignment = NSTextAlignmentLeft;
//	}
//	
//	self.textField.frame = editFrame;
//}

@end
